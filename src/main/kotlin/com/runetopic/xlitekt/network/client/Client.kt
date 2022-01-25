package com.runetopic.xlitekt.network.client

import com.github.michaelbull.logging.InlineLogger
import com.runetopic.cryptography.isaac.ISAAC
import com.runetopic.xlitekt.network.event.ReadEvent
import com.runetopic.xlitekt.network.event.WriteEvent
import com.runetopic.xlitekt.network.handler.EventHandler
import com.runetopic.xlitekt.network.handler.HandshakeEventHandler
import com.runetopic.xlitekt.network.packet.Packet
import com.runetopic.xlitekt.network.pipeline.EventPipeline
import com.runetopic.xlitekt.network.pipeline.GameEventPipeline
import com.runetopic.xlitekt.network.pipeline.HandshakeEventPipeline
import com.runetopic.xlitekt.plugin.ktor.inject
import io.ktor.network.sockets.Socket
import io.ktor.util.reflect.instanceOf
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.ByteWriteChannel
import kotlinx.coroutines.TimeoutCancellationException
import java.net.SocketException

class Client(
    private val socket: Socket,
    val readChannel: ByteReadChannel,
    val writeChannel: ByteWriteChannel
) {
    private val logger = InlineLogger()
    private var eventPipeline: EventPipeline<ReadEvent, WriteEvent> = useEventPipeline(inject<HandshakeEventPipeline>())
    private var eventHandler: EventHandler<ReadEvent, WriteEvent> = useEventHandler(inject<HandshakeEventHandler>())
    private var connected: Boolean = true

    var clientCipher: ISAAC? = null
    var serverCipher: ISAAC? = null
    val seed = ((Math.random() * 99999999.0).toLong() shl 32) + (Math.random() * 99999999.0).toLong()
    var connectedToJs5 = false
    var loggedIn = false

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

    suspend fun writePacket(packet: Packet) = eventPipeline.write(this, WriteEvent.GameWriteEvent(packet.opcode(), packet.size(), packet.builder().build()))

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
