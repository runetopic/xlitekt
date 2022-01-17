package com.runetopic.xlitekt.network

import io.ktor.network.selector.ActorSelectorManager
import io.ktor.network.sockets.aSocket
import io.ktor.network.sockets.openReadChannel
import io.ktor.network.sockets.openWriteChannel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import store
import java.net.InetSocketAddress
import java.util.concurrent.Executors

fun startListeningOnPort(port: Int) = runBlocking {
    val dispatcher = ActorSelectorManager(Executors.newCachedThreadPool().asCoroutineDispatcher())
    val server = aSocket(dispatcher).tcp().bind(InetSocketAddress(port))
    while (true) {
        val socket = server.accept() // this is connect

        val client = Client(
            store,
            socket,
            socket.openReadChannel(),
            socket.openWriteChannel()
        )

        launch(Dispatchers.IO) { loopClientIO(client) }
    }
}

private suspend fun loopClientIO(client: Client) {
    while (client.active) {
        try {
            client.pipeline!!.read(client)?.let { readEvent ->
                client.reactor!!.process(client, readEvent)?.let { writeEvent ->
                    client.pipeline!!.write(client, writeEvent)
                } ?: client.disconnect()
            } ?: client.disconnect()
        } catch (exception: Exception) {
            println("Exception caught with ${exception.message}")
            client.disconnect()
        }
    }
}
