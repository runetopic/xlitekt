package xlitekt.game.actor.player

import com.github.michaelbull.logging.InlineLogger
import com.runetopic.cache.store.Js5Store
import com.runetopic.cryptography.isaac.ISAAC
import io.ktor.network.sockets.Socket
import io.ktor.server.application.ApplicationEnvironment
import io.ktor.util.reflect.instanceOf
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.ByteWriteChannel
import io.ktor.utils.io.ClosedWriteChannelException
import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.readBytes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.runBlocking
import xlitekt.game.packet.Packet
import xlitekt.game.packet.assembler.PacketAssemblerListener
import xlitekt.game.packet.disassembler.handler.PacketHandler
import xlitekt.game.packet.disassembler.handler.PacketHandlerListener
import xlitekt.game.world.World
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.buffer.writeBytes
import xlitekt.shared.buffer.writeShort
import xlitekt.shared.inject
import java.io.IOException
import java.net.SocketException
import java.util.Collections
import kotlin.reflect.KClass

/**
 * @author Jordan Abraham
 */
class Client(
    val socket: Socket? = null,
    val readChannel: ByteReadChannel? = null,
    val writeChannel: ByteWriteChannel? = null
) {
    val logger = InlineLogger()
    val seed = ((Math.random() * 99999999.0).toLong() shl 32) + (Math.random() * 99999999.0).toLong()
    var clientCipher: ISAAC? = null
    var serverCipher: ISAAC? = null
    var player: Player? = null // TODO Figure out a way to not have the player here.

    private val writePool = Collections.synchronizedList(mutableListOf<Packet>())
    private val readPool = mutableMapOf<KClass<*>, PacketHandler<Packet>>()

    fun disconnect(reason: String) {
        logger.debug { "Client disconnected for reason={$reason}." }
        player?.let(world::requestLogout)
    }

    fun setIsaacCiphers(clientCipher: ISAAC, serverCipher: ISAAC) {
        if (this.clientCipher != null || this.serverCipher != null) {
            return disconnect("Client or server cipher is already set.")
        }
        this.clientCipher = clientCipher
        this.serverCipher = serverCipher
    }

    fun handleException(exception: Exception) = when {
        exception.instanceOf(TimeoutCancellationException::class) -> disconnect("Client timed out.")
        exception.instanceOf(SocketException::class) -> disconnect("Connection reset.")
        exception.instanceOf(ClosedWriteChannelException::class) -> disconnect("The channel was closed.")
        exception.instanceOf(ClosedReceiveChannelException::class) -> disconnect("The channel was closed.")
        exception.instanceOf(IOException::class) -> disconnect("Client IO exception caught.")
        else -> {
            logger.error(exception) { "Exception caught during client IO Events." }
            disconnect(exception.message.toString())
        }
    }

    fun addToWritePool(packet: Packet) {
        writePool += packet
    }

    fun addToReadPool(packetHandler: PacketHandler<Packet>) {
        // We use a map because we can replace keys if there are multiple requests of the same type.
        readPool[packetHandler.packet::class] = packetHandler
    }

    fun invokeAndClearWritePool() {
        writePool.onEach {
            val assembler = PacketAssemblerListener.listeners[it::class]
            if (assembler == null) {
                disconnect("Unhandled packet found when trying to write. Packet was $it.")
                return@onEach
            }
            runBlocking(Dispatchers.IO) {
                val packet = buildPacket {
                    val invoke = assembler.packet.invoke(it)
                    if (assembler.opcode > Byte.MAX_VALUE) {
                        writeByte { (Byte.MAX_VALUE + 1) + (serverCipher?.getNext() ?: 0) }
                    }
                    writeByte { 0xff and assembler.opcode + (serverCipher?.getNext() ?: 0) }
                    val size = assembler.size
                    if (size == -1) writeByte(invoke.remaining::toInt)
                    else if (size == -2) writeShort(invoke.remaining::toInt)
                    writeBytes(invoke::readBytes)
                }
                writePacket(packet)
            }
        }.also(MutableList<Packet>::clear)
        writeChannel?.flush()
    }

    fun invokeAndClearReadPool() {
        readPool.values.onEach { PacketHandlerListener.listeners[it.packet::class]?.invoke(it) }.also(MutableCollection<PacketHandler<Packet>>::clear)
    }

    private suspend fun writePacket(packet: ByteReadPacket) = writeChannel?.apply {
        if (isClosedForWrite) return@apply disconnect("Write channel closed.")
        writePacket(packet)
    }

    companion object {
        private val environment by inject<ApplicationEnvironment>()
        val majorBuild = environment.config.property("game.build.major").getString().toInt()
        val minorBuild = environment.config.property("game.build.minor").getString().toInt()
        val rsaExponent = environment.config.property("game.rsa.exponent").getString()
        val rsaModulus = environment.config.property("game.rsa.modulus").getString()
        val token = environment.config.property("game.build.token").getString()
        val sizes = environment.config.property("game.packet.sizes").getList().map(String::toInt)

        val store by inject<Js5Store>()
        val checksums = store.checksumsWithoutRSA()

        val world by inject<World>()
    }
}
