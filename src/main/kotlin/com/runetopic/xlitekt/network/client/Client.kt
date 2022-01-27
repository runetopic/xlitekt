package com.runetopic.xlitekt.network.client

import com.github.michaelbull.logging.InlineLogger
import com.runetopic.cryptography.isaac.ISAAC
import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.network.event.ReadEvent
import com.runetopic.xlitekt.network.event.WriteEvent
import com.runetopic.xlitekt.network.handler.EventHandler
import com.runetopic.xlitekt.network.handler.HandshakeEventHandler
import com.runetopic.xlitekt.network.packet.Packet
import com.runetopic.xlitekt.network.packet.RegisteredPackets.assemblers
import com.runetopic.xlitekt.network.packet.RegisteredPackets.disassemblers
import com.runetopic.xlitekt.network.packet.RegisteredPackets.handlers
import com.runetopic.xlitekt.network.pipeline.EventPipeline
import com.runetopic.xlitekt.network.pipeline.GameEventPipeline
import com.runetopic.xlitekt.network.pipeline.HandshakeEventPipeline
import com.runetopic.xlitekt.plugin.ktor.inject
import io.ktor.network.sockets.Socket
import io.ktor.util.reflect.instanceOf
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.ByteWriteChannel
import io.ktor.utils.io.core.ByteReadPacket
import kotlinx.coroutines.TimeoutCancellationException
import java.net.SocketException

/**
 * @author Jordan Abraham
 */
class Client(
    private val socket: Socket,
    val readChannel: ByteReadChannel,
    val writeChannel: ByteWriteChannel
) {
    private val logger = InlineLogger()
    private var eventPipeline: EventPipeline<ReadEvent, WriteEvent> = useEventPipeline(inject<HandshakeEventPipeline>())
    private var eventHandler: EventHandler<ReadEvent, WriteEvent> = useEventHandler(inject<HandshakeEventHandler>())
    private var connected: Boolean = true

    val seed = ((Math.random() * 99999999.0).toLong() shl 32) + (Math.random() * 99999999.0).toLong()
    var clientCipher: ISAAC? = null
    var serverCipher: ISAAC? = null
    var connectedToJs5 = false
    var loggedIn = false
    var player: Player? = null

    fun disconnect(reason: String) {
        connected = false
        socket.close()
        logger.info { "Client disconnected for reason={$reason}." }
    }

    fun setIsaacCiphers(clientCipher: ISAAC, serverCipher: ISAAC) {
        if (this.clientCipher != null || this.serverCipher != null) {
            disconnect("Client or server cipher is already set.")
            return
        }
        this.clientCipher = clientCipher
        this.serverCipher = serverCipher
    }

    suspend fun start() {
        while (connected) {
            try {
                if (eventPipeline.instanceOf(GameEventPipeline::class)) {
                    eventPipeline.read(this)?.let {
                        eventHandler.handleEvent(this, it)
                    }
                } else {
                    eventPipeline.read(this)?.let { read ->
                        eventHandler.handleEvent(this, read)?.let { write ->
                            eventPipeline.write(this, write)
                        } ?: disconnect("Event handler returned null for $eventHandler.")
                    } ?: disconnect("Read event returned null for $eventPipeline.")
                }
            } catch (exception: Exception) {
                when {
                    exception.instanceOf(TimeoutCancellationException::class) -> disconnect("Client timed out.")
                    exception.instanceOf(SocketException::class) -> disconnect("Connection reset.")
                    else -> {
                        logger.error(exception) { "Exception caught during client IO Events." }
                        disconnect(exception.message.toString())
                    }
                }
            }
        }
    }

    suspend fun writeResponse(response: Int) {
        writeChannel.writeByte(response.toByte())
        writeChannel.flush()
    }

    suspend fun writePacket(message: Packet) {
        val packet = assemblers[message::class] ?: return disconnect("Unhandled message found when trying to write packet. Message was $message.")
        eventPipeline.write(this, WriteEvent.GameWriteEvent(packet.opcode, packet.size, packet.assemblePacket(message).build()))
    }

    suspend fun readPacket(opcode: Int, packet: ByteReadPacket) {
        val player = player ?: return disconnect("Player is not established when attempting to read and handle packet. Opcode was $opcode.")
        val disassembler = disassemblers.firstOrNull { it.opcode == opcode } ?: return logger.info { "Unhandled packet opcode when looking for decoder. Opcode was $opcode." }
        val disassembledPacket = disassembler.disassemblePacket(packet)
        val handler = handlers[disassembledPacket::class] ?: return logger.info { "Unhandled packet opcode found when looking for handler. Opcode was $opcode." }
        handler.handlePacket(player, disassembledPacket)
    }

    @Suppress("UNCHECKED_CAST")
    fun useEventPipeline(eventPipeline: Lazy<EventPipeline<out ReadEvent, out WriteEvent>>): EventPipeline<ReadEvent, WriteEvent> {
        this.eventPipeline = eventPipeline.value as EventPipeline<ReadEvent, WriteEvent>
        return this.eventPipeline
    }

    @Suppress("UNCHECKED_CAST")
    fun useEventHandler(eventHandler: Lazy<EventHandler<*, *>>): EventHandler<ReadEvent, WriteEvent> {
        this.eventHandler = eventHandler.value as EventHandler<ReadEvent, WriteEvent>
        return this.eventHandler
    }
}
