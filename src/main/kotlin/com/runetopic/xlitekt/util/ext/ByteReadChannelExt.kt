package com.runetopic.xlitekt.util.ext

import com.runetopic.cryptography.isaac.ISAAC
import io.ktor.utils.io.ByteReadChannel

suspend fun ByteReadChannel.readPacketOpcode(isaac: ISAAC): Int = (0xff and (readByte().toInt() and 0xff) - isaac.getNext()).let {
    if (it > Byte.MAX_VALUE) (it - (Byte.MAX_VALUE + 1) shl 8) + ((readByte().toInt() and 0xff) - isaac.getNext()) else it
}

suspend fun ByteReadChannel.readPacketSize(input: Int): Int = when (input) {
    -1, -2 -> {
        val bytes = if (input == -1) Byte.SIZE_BYTES else Short.SIZE_BYTES
        when (input) {
            -1 -> if (availableForRead >= bytes) readByte().toInt() and 0xff else availableForRead
            -2 -> if (availableForRead >= bytes) readShort().toInt() and 0xffff else availableForRead
            else -> throw IllegalStateException("Input dynamic packet size must be either -1 or -2.")
        }
    }
    else -> input
}
