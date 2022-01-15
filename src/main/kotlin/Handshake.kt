import io.ktor.network.sockets.Socket
import io.ktor.network.sockets.isClosed
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.ByteWriteChannel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Handshake(
    private val socket: Socket
) : ReadWriteEvent {

    var response = 6

    override suspend fun read(channel: ByteReadChannel) {
        val opcode = channel.readByte().toInt()
        println(opcode)
        if (opcode == 15) {
            val version = channel.readInt()
            println(version)
            if (version == 202) {
                response = 0
                closeSocket(socket)

                //response = 0
            }
        }
    }

    override suspend fun write(channel: ByteWriteChannel) {
        println(response)
        println("WRITE")
        channel.writeByte(response.toByte())
    }
}
