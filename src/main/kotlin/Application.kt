import com.runetopic.cache.store.Js5Store
import io.ktor.network.selector.ActorSelectorManager
import io.ktor.network.sockets.aSocket
import io.ktor.network.sockets.openReadChannel
import io.ktor.network.sockets.openWriteChannel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.net.InetSocketAddress
import java.nio.file.Path
import kotlin.time.measureTime

fun main() {
    runBlocking {
        val store = Js5Store(path = Path.of("${System.getProperty("user.home")}/202/"), parallel = true)

        val server = aSocket(ActorSelectorManager(Dispatchers.IO)).tcp().bind(InetSocketAddress(43594))
        println("online!")

        while (true) {
            val socket = server.accept() // this blocks

            launch {
                val client = Client(
                    store,
                    socket,
                    socket.openReadChannel(),
                    socket.openWriteChannel()
                )

                try {
                    while (client.active) {
                        val time = measureTime {
                            client.pipeline!!.read(client)?.let { readEvent ->
                                client.reactor!!.process(client, readEvent)?.let { writeEvent ->
                                    client.pipeline!!.write(client, writeEvent)
                                } ?: client.disconnect()
                            } ?: client.disconnect()
                        }
                        println(time)
                    }
                } catch (exception: Exception) {
                    println("Exception caught with ${exception.message}")
                    client.disconnect()
                }
            }
        }
    }
}
