package com.runetopic.xlitekt.network

import com.runetopic.cache.store.Js5Store
import com.runetopic.xlitekt.network.event.ReadEvent
import com.runetopic.xlitekt.network.event.WriteEvent
import com.runetopic.xlitekt.network.pipeline.HandshakePipeline
import com.runetopic.xlitekt.network.pipeline.Pipeline
import com.runetopic.xlitekt.network.reactor.HandshakeReactor
import com.runetopic.xlitekt.network.reactor.Reactor
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
    var pipeline: Pipeline<ReadEvent, WriteEvent>? = null
    var reactor: Reactor<ReadEvent, WriteEvent>? = null
    var connectedToJs5 = false
    var loggedIn = false

    init {
        usePipeline(HandshakePipeline())
        useReactor(HandshakeReactor())
        active = true
    }

    fun disconnect() {
        active = false
        socket.close()
        println("com.runetopic.xlitekt.network.Client disconnected.")
    }

    @Suppress("UNCHECKED_CAST")
    fun usePipeline(pipeline: Pipeline<*, *>) {
        this.pipeline = pipeline as Pipeline<ReadEvent, WriteEvent>
    }

    @Suppress("UNCHECKED_CAST")
    fun useReactor(reactor: Reactor<*, *>) {
        this.reactor = reactor as Reactor<ReadEvent, WriteEvent>
    }
}
