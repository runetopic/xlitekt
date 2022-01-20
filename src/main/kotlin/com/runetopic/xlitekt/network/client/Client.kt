package com.runetopic.xlitekt.network.client

import com.runetopic.xlitekt.network.event.ReadEvent
import com.runetopic.xlitekt.network.event.WriteEvent
import com.runetopic.xlitekt.network.handler.EventHandler
import com.runetopic.xlitekt.network.handler.HandshakeEventHandler
import com.runetopic.xlitekt.network.pipeline.EventPipeline
import com.runetopic.xlitekt.network.pipeline.HandshakeEventPipeline
import com.runetopic.xlitekt.plugin.ktor.inject
import io.ktor.network.sockets.Socket
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.ByteWriteChannel
import org.slf4j.Logger

class Client(
    private val socket: Socket,
    val readChannel: ByteReadChannel,
    val writeChannel: ByteWriteChannel
) {
    val seed = ((Math.random() * 99999999.0).toLong() shl 32) + (Math.random() * 99999999.0).toLong()
    var connected = false
    var eventPipeline: EventPipeline<ReadEvent, WriteEvent> = useEventPipeline(inject<HandshakeEventPipeline>())
    var eventHandler: EventHandler<ReadEvent, WriteEvent> = useEventHandler(inject<HandshakeEventHandler>())
    var connectedToJs5 = false
    var loggedIn = false

    private val logger by inject<Logger>()

    suspend fun writeResponse(response: Int) {
        writeChannel.writeByte(response.toByte())
        writeChannel.flush()
    }

    fun disconnect() {
        connected = false
        socket.close()
        logger.info("Client disconnected.")
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
