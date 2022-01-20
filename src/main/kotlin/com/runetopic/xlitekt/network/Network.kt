package com.runetopic.xlitekt.network

import com.runetopic.xlitekt.network.client.Client
import com.runetopic.xlitekt.network.handler.HandshakeEventHandler
import com.runetopic.xlitekt.network.handler.JS5EventHandler
import com.runetopic.xlitekt.network.handler.LoginEventHandler
import com.runetopic.xlitekt.network.pipeline.HandshakeEventPipeline
import com.runetopic.xlitekt.network.pipeline.JS5EventPipeline
import com.runetopic.xlitekt.network.pipeline.LoginEventPipeline
import com.runetopic.xlitekt.plugin.ktor.inject
import io.ktor.network.selector.ActorSelectorManager
import io.ktor.network.sockets.aSocket
import io.ktor.network.sockets.openReadChannel
import io.ktor.network.sockets.openWriteChannel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.dsl.module
import org.slf4j.Logger
import java.net.InetSocketAddress
import java.util.concurrent.Executors

val networkModule = module {
    single { HandshakeEventPipeline() }
    single { HandshakeEventHandler() }
    single { JS5EventPipeline() }
    single { JS5EventHandler() }
    single { LoginEventPipeline() }
    single { LoginEventHandler() }
}

fun awaitOnPort(port: Int) = runBlocking {
    val dispatcher = ActorSelectorManager(Executors.newCachedThreadPool().asCoroutineDispatcher())
    val server = aSocket(dispatcher).tcp().bind(InetSocketAddress(port))
    while (true) {
        val socket = server.accept()

        val client = Client(
            socket,
            socket.openReadChannel(),
            socket.openWriteChannel()
        )

        client.connected = true

        launch(Dispatchers.IO) { startClientIOEvents(client) }
    }
}

private suspend fun startClientIOEvents(client: Client) = with(client) {
    while (connected) {
        try {
            eventPipeline.read(this)?.let { read ->
                eventHandler.handleEvent(this, read)?.let { write ->
                    eventPipeline.write(this, write)
                } ?: disconnect()
            } ?: disconnect()
        } catch (exception: Exception) {
            inject<Logger>().value.error("Exception caught during client IO Events.", exception)
            disconnect()
        }
    }
}
