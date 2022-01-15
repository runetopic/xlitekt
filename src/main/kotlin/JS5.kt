import io.ktor.network.sockets.Socket
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.ByteWriteChannel

class JS5(socket: Socket) : ReadWriteEvent {
    override suspend fun read(channel: ByteReadChannel) {
        println("Js5 read")
        //if (channel.availableForRead < 4) return
        val opcode = channel.readByte().toInt()
        println(opcode)
        if (opcode == 0 || opcode == 1) {
            println("DO js5")
            val indexId = channel.readByte().toInt() and 0xFF
            val groupId = channel.readShort().toInt()
            println("$indexId, $groupId")
        } else {
            assert(channel.discard(3) == 3L)
        }
    }

    override suspend fun write(channel: ByteWriteChannel) {
        println("Write js5")
    }
}
