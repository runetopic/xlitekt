package com.runetopic.xlitekt.util.ext

import com.runetopic.cryptography.isaac.ISAAC
import io.ktor.utils.io.ByteWriteChannel

suspend fun ByteWriteChannel.writePacketOpcode(isaac: ISAAC, opcode: Int) {
    if (opcode > Byte.MAX_VALUE) {
        writeByte(((Byte.MAX_VALUE + 1) + isaac.getNext()).toByte())
    }
    writeByte((0xFF and opcode + isaac.getNext()).toByte())
}

suspend fun ByteWriteChannel.writePacketSize(input: Int, size: Int) {
    when (input) {
        -1 -> writeByte(size.toByte())
        -2 -> writeShort(size.toShort())
    }
}
