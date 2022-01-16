import com.runetopic.cache.store.Js5Store
import com.runetopic.xlitekt.network.HandshakePipeline
import com.runetopic.xlitekt.network.HandshakeReactor
import com.runetopic.xlitekt.network.Pipeline
import com.runetopic.xlitekt.network.Reactor
import com.runetopic.xlitekt.network.ReadEvent
import com.runetopic.xlitekt.network.WriteEvent
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

    init {
        usePipeline(HandshakePipeline())
        useReactor(HandshakeReactor())
        active = true
    }

    fun disconnect() {
        active = false
        socket.close()
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
