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
import io.ktor.utils.io.close
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.runBlocking
import org.jctools.maps.NonBlockingHashSet
import xlitekt.game.packet.MoveGameClickPacket
import xlitekt.game.packet.MoveMinimapClickPacket
import xlitekt.game.packet.Packet
import xlitekt.game.packet.PublicChatPacket
import xlitekt.game.packet.assembler.PacketAssemblerListener
import xlitekt.game.packet.disassembler.PacketDisassemblerListener
import xlitekt.game.packet.disassembler.handler.PacketHandler
import xlitekt.game.packet.disassembler.handler.PacketHandlerListener
import xlitekt.game.world.World
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.buffer.writeShort
import xlitekt.shared.inject
import xlitekt.shared.lazyInject
import java.io.IOException
import java.net.SocketException
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
class Client(
    private val socket: Socket? = null,
    val readChannel: ByteReadChannel? = null,
    val writeChannel: ByteWriteChannel? = null
) {
    val logger = InlineLogger()
    val seed = ((Math.random() * 99999999.0).toLong() shl 32) + (Math.random() * 99999999.0).toLong()
    lateinit var clientCipher: ISAAC
    private lateinit var serverCipher: ISAAC
    lateinit var player: Player

    private val readPool = NonBlockingHashSet<PacketHandler<Packet>>()
    private val writePool = ByteBuffer.allocateDirect(40000)

    fun disconnect(reason: String) {
        logger.debug { "Client disconnected for reason={$reason}." }
        if (::player.isInitialized) {
            player.let(lazyInject<World>()::requestLogout)
        }
        writeChannel?.close()
        socket?.close()
        readPool.clear()
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
        val assembler = packetAssemblerListener[packet::class]
        if (assembler == null) {
            disconnect("Unhandled packet found when trying to write. Packet was $packet.")
            return
        }
        if (assembler.opcode > Byte.MAX_VALUE) {
            writePool.writeByte((128 + serverCipher.getNext()))
        }
        writePool.writeByte((assembler.opcode + serverCipher.getNext() and 0xff))
        if (assembler.size != -1 && assembler.size != -2) {
            assembler.packet.invoke(packet, writePool)
        } else {
            val startPos = writePool.position()
            val offset = startPos + if (assembler.size == -1) 1 else 2
            writePool.position(offset)
            assembler.packet.invoke(packet, writePool)
            val endPos = writePool.position()
            val size = endPos - offset
            writePool.position(startPos)
            if (assembler.size == -1) writePool.writeByte(size)
            else writePool.writeShort(size)
            writePool.position(endPos)
        }
    }

    internal fun addToReadPool(packetHandler: PacketHandler<Packet>) {
        readPool.removeIf {
            it::packet::class == MoveGameClickPacket::class || it::packet::class == MoveMinimapClickPacket::class || it::packet::class == PublicChatPacket::class
        }
        readPool.add(packetHandler)
    }

    internal fun invokeAndClearWritePool() {
        writeChannel?.let {
            if (it.isClosedForWrite) return
            // This way we only have to suspend once per client.
            runBlocking(Dispatchers.IO) {
                it.writeFully(writePool.flip())
            }
            it.flush()
        }
        // Set write pool back to default.
        writePool.clear()
    }

    internal fun invokeAndClearReadPool() {
        for (packet in readPool) {
            packetHandlerListener[packet.packet::class]?.invoke(packet)
        }
        readPool.clear()
    }

    companion object {
        private val environment by inject<ApplicationEnvironment>()
        private val packetHandlerListener by inject<PacketHandlerListener>()
        private val packetAssemblerListener by inject<PacketAssemblerListener>()
        val packetDisassemblerListener by inject<PacketDisassemblerListener>()
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
