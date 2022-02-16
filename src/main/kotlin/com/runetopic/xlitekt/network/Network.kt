package com.runetopic.xlitekt.network

import com.github.michaelbull.logging.InlineLogger
import com.runetopic.xlitekt.network.client.Client
import com.runetopic.xlitekt.shared.buffer.readHandshake
import io.ktor.network.selector.ActorSelectorManager
import io.ktor.network.sockets.aSocket
import io.ktor.network.sockets.openReadChannel
import io.ktor.network.sockets.openWriteChannel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.dsl.module
import java.net.InetSocketAddress
import java.util.concurrent.Executors

val networkModule = module(createdAtStart = true) {
    single { Network() }
}

/**
 * @author Jordan Abraham
 */
class Network {
    private val logger = InlineLogger()
    private val dispatcher = Executors.newCachedThreadPool().asCoroutineDispatcher()
    private val selector = ActorSelectorManager(dispatcher)

    fun awaitOnPort(port: Int) = runBlocking {
        val server = aSocket(selector).tcp().bind(InetSocketAddress(port))
        logger.info { "Network is now accepting connections." }

        while (true) {
            val socket = server.accept()

            val client = Client(
                socket,
                socket.openReadChannel(),
                socket.openWriteChannel()
            )
            launch(Dispatchers.IO) { client.readHandshake() }
        }
    }

    fun shutdownGracefully() {
        logger.debug { "Shutting down network selector..." }
        selector.close()
        logger.debug { "Shutting down network dispatcher..." }
        dispatcher.close()
    }
}
