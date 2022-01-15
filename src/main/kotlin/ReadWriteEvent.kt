import io.ktor.network.sockets.Connection
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.ByteWriteChannel

interface ReadWriteEvent {
    suspend fun read(channel: ByteReadChannel)
    suspend fun write(channel: ByteWriteChannel)
}
