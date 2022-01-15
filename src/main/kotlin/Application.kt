import io.ktor.network.selector.ActorSelectorManager
import io.ktor.network.sockets.Socket
import io.ktor.network.sockets.aSocket
import io.ktor.network.sockets.isClosed
import io.ktor.network.sockets.openReadChannel
import io.ktor.network.sockets.openWriteChannel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.net.InetSocketAddress
import kotlinx.coroutines.withContext

fun main() {
    println("Hello World")

    runBlocking {
        val server = aSocket(ActorSelectorManager(Dispatchers.IO)).tcp().bind(InetSocketAddress(43594))

        while (true) {
            val socket = server.accept() // this blocks

            launch {
                val readChannel = socket.openReadChannel()
                val writeChannel = socket.openWriteChannel(autoFlush = true)

                while (socket.isClosed.not()) {
                    Handshake(socket).let {
                        it.read(readChannel)
                        it.write(writeChannel)
                    }

                    JS5(socket).let {
                        it.read(readChannel)
                        it.write(writeChannel)
                    }
                }

//                Handshake(socket).let { handshake ->
//                    handshake.read(readChannel)
//                    handshake.write(writeChannel)
//
//                    if (handshake.response == 0) {
//                        while (true) {
//                            JS5().let { js5 ->
//                                js5.read(readChannel)
//                                js5.write(writeChannel)
//                            }
//                        }
//                    }
//                }
            }
        }
    }
}

suspend fun closeSocket(socket: Socket) = withContext(Dispatchers.IO) {
    socket.close()
}
