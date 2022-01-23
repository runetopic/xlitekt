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
    var connected: Boolean = true
    val seed = ((Math.random() * 99999999.0).toLong() shl 32) + (Math.random() * 99999999.0).toLong()
    var eventPipeline: EventPipeline<ReadEvent, WriteEvent>? = null
    var eventHandler: EventHandler<ReadEvent, WriteEvent>? = null
    var connectedToJs5 = false
    var loggedIn = false

    private val logger by inject<Logger>()

    init {
        useEventPipeline<ReadEvent.HandshakeReadEvent, WriteEvent.HandshakeWriteEvent>(inject<HandshakeEventPipeline>())
        useEventHandler<ReadEvent.HandshakeReadEvent, WriteEvent.HandshakeWriteEvent>(inject<HandshakeEventHandler>())
    }

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
    inline fun <reified R : ReadEvent, reified W : WriteEvent> useEventPipeline(eventPipeline: Lazy<EventPipeline<*, *>>): EventPipeline<R, W> {
        this.eventPipeline = eventPipeline.value as EventPipeline<R, W>
        return this.eventPipeline as EventPipeline<R, W>
    }

    @Suppress("UNCHECKED_CAST")
    inline fun <reified R : ReadEvent, reified W : WriteEvent> useEventHandler(eventHandler: Lazy<EventHandler<*, *>>): EventHandler<R, W> {
        this.eventHandler = eventHandler.value as EventHandler<R, W>
        return this.eventHandler as EventHandler<R, W>
    }
}
