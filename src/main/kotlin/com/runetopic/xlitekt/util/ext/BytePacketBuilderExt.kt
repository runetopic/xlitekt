package com.runetopic.xlitekt.util.ext

import io.ktor.utils.io.core.BytePacketBuilder
import io.ktor.utils.io.core.writeFully
import io.ktor.utils.io.core.writeShort

fun BytePacketBuilder.writeStringCp1252NullTerminated(value: String) {
    value.chars().forEach { writeByte(it.toByte()) }
    writeByte(0)
}

fun BytePacketBuilder.writeBytesAdd(bytes: ByteArray) {
    bytes.forEach { writeByteAdd(it) }
}

fun BytePacketBuilder.writeSmart(value: Int) {
    if (value > 128) writeShort(value.toShort()) else writeByte(value.toByte())
}

fun BytePacketBuilder.writeByteSubtract(value: Byte) = writeByte((128 - value).toByte())
fun BytePacketBuilder.writeByteAdd(value: Byte) = writeByte((value + 128).toByte())

fun BytePacketBuilder.writeShortAdd(value: Short) {
    writeByte((value.toInt() shr 8).toByte())
    writeByteAdd(value.toByte())
}

fun BytePacketBuilder.withBitAccess(block: BitAccess.() -> Unit) {
    val accessor = BitAccess()
    block.invoke(accessor)
    accessor.write(this)
}

class BitAccess {
    private var bitIndex = 0
    private val data = ByteArray(4096 * 2)

    fun writeBit(value: Boolean) = writeBits(1, if (value) 1 else 0)

    fun writeBits(count: Int, value: Int) {
        var numBits = count

        var byteIndex = bitIndex shr 3
        var bitOffset = 8 - (bitIndex and 7)
        bitIndex += numBits

        var tmp: Int
        var max: Int
        while (numBits > bitOffset) {
            tmp = data[byteIndex].toInt()
            max = BIT_MASKS[bitOffset]
            tmp = tmp and max.inv() or (value shr numBits - bitOffset and max)
            data[byteIndex++] = tmp.toByte()
            numBits -= bitOffset
            bitOffset = 8
        }

        tmp = data[byteIndex].toInt()
        max = BIT_MASKS[numBits]
        if (numBits == bitOffset) {
            tmp = tmp and max.inv() or (value and max)
        } else {
            tmp = tmp and (max shl bitOffset - numBits).inv()
            tmp = tmp or (value and max shl bitOffset - numBits)
        }
        data[byteIndex] = tmp.toByte()
    }

    fun write(builder: BytePacketBuilder) = builder.writeFully(data, 0, (bitIndex + 7) / 8)

    companion object {
        private val BIT_MASKS = IntArray(32)

        init {
            for (i in BIT_MASKS.indices) {
                BIT_MASKS[i] = (1 shl i) - 1
            }
        }
    }
}
