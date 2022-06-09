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
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeFully
import io.ktor.utils.io.core.writeShort
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.runBlocking
import org.jctools.maps.NonBlockingHashSet
import xlitekt.game.packet.MovementPacket
import xlitekt.game.packet.Packet
import xlitekt.game.packet.PublicChatPacket
import xlitekt.game.packet.assembler.PacketAssemblerListener
import xlitekt.game.packet.disassembler.handler.PacketHandler
import xlitekt.game.packet.disassembler.handler.PacketHandlerListener
import xlitekt.game.world.World
import xlitekt.shared.inject
import xlitekt.shared.lazy
import java.io.IOException
import java.net.SocketException

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
    lateinit var clientCipher: ISAAC
    lateinit var serverCipher: ISAAC
    lateinit var player: Player

    private val writePool = ArrayList<Packet>(64)
    private val readPool = NonBlockingHashSet<PacketHandler<Packet>>()

    fun disconnect(reason: String) {
        logger.debug { "Client disconnected for reason={$reason}." }
        if (::player.isInitialized) {
            player.let(lazy<World>()::requestLogout)
        }
        socket?.close()
    }

    fun setIsaacCiphers(clientCipher: ISAAC, serverCipher: ISAAC) {
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

    internal fun addToWritePool(packet: Packet) {
        writePool.add(packet)
    }

    internal fun addToReadPool(packetHandler: PacketHandler<Packet>) {
        readPool.removeIf { it::packet::class == MovementPacket::class }
        readPool.removeIf { it::packet::class == PublicChatPacket::class }
        readPool.add(packetHandler)
    }

    internal fun invokeAndClearWritePool() {
        val readPacket = buildPacket {
            for (packet in writePool) {
                val assembler = PacketAssemblerListener.listeners[packet::class]
                if (assembler == null) {
                    disconnect("Unhandled packet found when trying to write. Packet was $packet.")
                    return
                }
                val invoke = assembler.packet.invoke(packet)
                if (assembler.opcode > Byte.MAX_VALUE) {
                    writeByte((128 + serverCipher.getNext()).toByte())
                }
                writeByte((assembler.opcode + serverCipher.getNext() and 0xff).toByte())
                if (assembler.size == -1) writeByte(invoke.size.toByte())
                else if (assembler.size == -2) writeShort(invoke.size.toShort())
                writeFully(invoke)
            }
            writePool.clear()
        }
        writeChannel?.let {
            // This way we only have to suspend once per client.
            runBlocking(Dispatchers.IO) {
                it.writePacket(readPacket)
            }
            it.flush()
        }
        readPacket.release()
    }

    internal fun invokeAndClearReadPool() {
        for (packet in readPool) {
            PacketHandlerListener.listeners[packet.packet::class]?.invoke(packet)
        }
        readPool.clear()
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
    }
}
