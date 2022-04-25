package xlitekt.network

import com.github.michaelbull.logging.InlineLogger
import io.ktor.network.selector.ActorSelectorManager
import io.ktor.network.sockets.aSocket
import io.ktor.network.sockets.openReadChannel
import io.ktor.network.sockets.openWriteChannel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import xlitekt.game.actor.player.Client
import xlitekt.network.client.readHandshake
import java.util.concurrent.Executors

/**
 * @author Jordan Abraham
 */
class Network {
    private val logger = InlineLogger()
    private val dispatcher = Executors.newCachedThreadPool().asCoroutineDispatcher()
    private val selector = ActorSelectorManager(dispatcher)

    fun awaitOnPort(port: Int) = runBlocking {
        val server = aSocket(selector).tcp().bind(
            hostname = "0.0.0.0",
            port = port
        ) {
            this.backlogSize = 2000
            this.reuseAddress = true
        }
        logger.info { "Awaiting client requests..." }

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

    fun shutdown() {
        logger.debug { "Shutting down network selector..." }
        selector.close()
        logger.debug { "Shutting down network dispatcher..." }
        dispatcher.close()
    }
}
