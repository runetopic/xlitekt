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
import io.ktor.utils.io.core.readBytes
import io.ktor.utils.io.writeFully
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.runBlocking
import xlitekt.game.packet.Packet
import xlitekt.game.packet.assembler.PacketAssemblerListener
import xlitekt.game.packet.disassembler.handler.PacketHandler
import xlitekt.game.packet.disassembler.handler.PacketHandlerListener
import xlitekt.game.world.World
import xlitekt.shared.buffer.writePacketOpcode
import xlitekt.shared.buffer.writePacketSize
import xlitekt.shared.inject
import java.io.IOException
import java.net.SocketException
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

    private val writePool = mutableListOf<Packet>()
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
            val packet = assembler.packet.invoke(it)
            runBlocking(Dispatchers.IO) {
                if (serverCipher == null) return@runBlocking
                writePacket(assembler.opcode, assembler.size, packet.readBytes(), serverCipher!!)
            }
            packet.release()
        }.also(MutableList<Packet>::clear)
        writeChannel?.flush()
    }

    fun invokeAndClearReadPool() {
        readPool.values.onEach { PacketHandlerListener.listeners[it.packet::class]?.invoke(it) }.also(MutableCollection<PacketHandler<Packet>>::clear)
    }

    private suspend fun writePacket(opcode: Int, size: Int, packet: ByteArray, cipher: ISAAC) = writeChannel?.apply {
        // if (isClosedForWrite) return@apply disconnect("Write channel closed.")
        // This write channel is null checked because bot client can use this.
        writePacketOpcode(cipher, opcode)
        if (size == -1 || size == -2) {
            writePacketSize(size, packet.size)
        }
        writeFully(packet)
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
