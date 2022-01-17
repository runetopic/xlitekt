package com.runetopic.xlitekt.client

import com.runetopic.cache.store.Js5Store
import com.runetopic.xlitekt.network.event.ReadEvent
import com.runetopic.xlitekt.network.event.WriteEvent
import com.runetopic.xlitekt.network.pipeline.HandshakeEventPipeline
import com.runetopic.xlitekt.network.pipeline.EventPipeline
import com.runetopic.xlitekt.network.handler.HandshakeEventHandler
import com.runetopic.xlitekt.network.handler.EventHandler
import io.ktor.network.sockets.Socket
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.ByteWriteChannel

class Client(
    val store: Js5Store,
    private val socket: Socket,
    val readChannel: ByteReadChannel,
    val writeChannel: ByteWriteChannel
) {
    var active = false
    var eventPipeline: EventPipeline<ReadEvent, WriteEvent>? = null
    var eventHandler: EventHandler<ReadEvent, WriteEvent>? = null
    var connectedToJs5 = false
    var loggedIn = false

    init {
        useEventPipeline(HandshakeEventPipeline())
        useEventHandler(HandshakeEventHandler())
        active = true
    }

    fun disconnect() {
        active = false
        socket.close()
        println("Client disconnected.")
    }

    @Suppress("UNCHECKED_CAST")
    fun useEventPipeline(eventPipeline: EventPipeline<*, *>) {
        this.eventPipeline = eventPipeline as EventPipeline<ReadEvent, WriteEvent>
    }

    @Suppress("UNCHECKED_CAST")
    fun useEventHandler(eventHandler: EventHandler<*, *>) {
        this.eventHandler = eventHandler as EventHandler<ReadEvent, WriteEvent>
    }
}
