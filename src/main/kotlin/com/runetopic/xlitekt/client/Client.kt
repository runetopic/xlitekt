package com.runetopic.xlitekt.client

import com.runetopic.xlitekt.network.event.ReadEvent
import com.runetopic.xlitekt.network.event.WriteEvent
import com.runetopic.xlitekt.network.handler.EventHandler
import com.runetopic.xlitekt.network.handler.HandshakeEventHandler
import com.runetopic.xlitekt.network.inject
import com.runetopic.xlitekt.network.pipeline.EventPipeline
import com.runetopic.xlitekt.network.pipeline.HandshakeEventPipeline
import io.ktor.network.sockets.Socket
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.ByteWriteChannel
import org.slf4j.Logger

class Client(
    private val socket: Socket,
    val readChannel: ByteReadChannel,
    val writeChannel: ByteWriteChannel
) {
    var active = false
    var eventPipeline: EventPipeline<ReadEvent, WriteEvent>? = null
    var eventHandler: EventHandler<ReadEvent, WriteEvent>? = null
    var connectedToJs5 = false
    var loggedIn = false

    private val logger by inject<Logger>()

    init {
        useEventPipeline(inject<HandshakeEventPipeline>())
        useEventHandler(inject<HandshakeEventHandler>())
        active = true
    }

    fun disconnect() {
        active = false
        socket.close()
        logger.info("Client disconnected.")
    }

    @Suppress("UNCHECKED_CAST")
    fun useEventPipeline(eventPipeline: Lazy<EventPipeline<*, *>>) {
        this.eventPipeline = eventPipeline.value as EventPipeline<ReadEvent, WriteEvent>
    }

    @Suppress("UNCHECKED_CAST")
    fun useEventHandler(eventHandler: Lazy<EventHandler<*, *>>) {
        this.eventHandler = eventHandler.value as EventHandler<ReadEvent, WriteEvent>
    }
}
