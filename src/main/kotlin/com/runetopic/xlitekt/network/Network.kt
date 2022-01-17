package com.runetopic.xlitekt.network

import com.runetopic.xlitekt.client.Client
import com.runetopic.xlitekt.network.handler.HandshakeEventHandler
import com.runetopic.xlitekt.network.handler.JS5EventHandler
import com.runetopic.xlitekt.network.pipeline.HandshakeEventPipeline
import com.runetopic.xlitekt.network.pipeline.JS5EventPipeline
import io.ktor.network.selector.ActorSelectorManager
import io.ktor.network.sockets.aSocket
import io.ktor.network.sockets.openReadChannel
import io.ktor.network.sockets.openWriteChannel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.dsl.module
import org.koin.mp.KoinPlatformTools
import java.net.InetSocketAddress
import java.util.concurrent.Executors
import org.slf4j.Logger

val networkModule = module {
    single { HandshakeEventPipeline() }
    single { HandshakeEventHandler() }
    single { JS5EventPipeline() }
    single { JS5EventHandler() }
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

        launch(Dispatchers.IO) { startClientIOEvents(client) }
    }
}

private suspend fun startClientIOEvents(client: Client) = with(client) {
    while (active) {
        try {
            eventPipeline!!.read(this)?.let { read ->
                eventHandler!!.handleEvent(this, read)?.let { write ->
                    eventPipeline!!.write(this, write)
                } ?: disconnect()
            } ?: disconnect()
        } catch (exception: Exception) {
            inject<Logger>().value.error("Exception caught with ${exception.message}")
            disconnect()
        }
    }
}

inline fun <reified T : Any> inject(
    qualifier: Qualifier? = null,
    mode: LazyThreadSafetyMode = KoinPlatformTools.defaultLazyMode(),
    noinline parameters: ParametersDefinition? = null
): Lazy<T> = KoinPlatformTools.defaultContext().get().inject(qualifier, mode, parameters)
