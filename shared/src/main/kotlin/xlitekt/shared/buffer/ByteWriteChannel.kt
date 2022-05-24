package xlitekt.shared.buffer

import com.runetopic.cryptography.isaac.ISAAC
import io.ktor.utils.io.ByteWriteChannel

suspend fun ByteWriteChannel.writePacketOpcode(isaac: ISAAC, opcode: Int) {
    if (opcode > Byte.MAX_VALUE) {
        writeByte(((Byte.MAX_VALUE + 1) + isaac.getNext()).toByte())
    }
    writeByte((0xff and opcode + isaac.getNext()).toByte())
}

suspend fun ByteWriteChannel.writePacketSize(input: Int, size: Int) = when (input) {
    -1 -> writeByte(size.toByte())
    -2 -> writeShort(size.toShort())
    else -> throw IllegalArgumentException("Attempting to write a packet with size failed. Size was $size.")
}
