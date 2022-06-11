package xlitekt.shared.buffer

import io.ktor.utils.io.core.BytePacketBuilder
import io.ktor.utils.io.core.readBytes
import io.ktor.utils.io.core.writeFully
import io.ktor.utils.io.core.writeShort
import io.ktor.utils.io.core.writeShortLittleEndian
import xlitekt.shared.toInt

/**
 * @author Jordan Abraham
 * Extension functions for the BytePacketBuilder class used for building packets that require a dynamically growing capacity.
 */
inline fun buildDynamicPacket(block: BytePacketBuilder.() -> Unit): ByteArray = BytePacketBuilder()
    .also { block.invoke(it) }
    .build()
    .readBytes()

fun BytePacketBuilder.writeStringCp1252NullTerminated(value: String) {
    value.toByteArray().forEach(::writeByte)
    writeByte(0)
}

fun BytePacketBuilder.writeByte(value: Int) {
    writeByte(value.toByte())
}

fun BytePacketBuilder.writeByteAdd(value: Int) {
    writeByte((value.toByte() + 128).toByte())
}

fun BytePacketBuilder.writeByteNegate(value: Int) {
    writeByte((-value.toByte()).toByte())
}

fun BytePacketBuilder.writeSmart(value: Int) {
    if (value > 128) writeShort(value.toShort()) else writeByte(value.toByte())
}

fun BytePacketBuilder.writeShort(value: Int) {
    writeShort(value.toShort())
}

fun BytePacketBuilder.writeShortLittleEndian(value: Int) {
    writeShortLittleEndian(value.toShort())
}

fun BytePacketBuilder.writeShortAdd(value: Int) {
    writeByte((value shr 8).toByte())
    writeByteAdd(value)
}

inline fun BytePacketBuilder.withBitAccess(block: BitAccess.() -> Unit) {
    val accessor = BitAccess()
    block.invoke(accessor)
    writeFully(accessor.data, 0, (accessor.bitIndex + 7) / 8)
}

class BitAccess {
    var bitIndex = 0
    val data = ByteArray(4096 * 2)

    fun writeBit(value: Boolean) {
        writeBits(1, value.toInt())
    }

    fun writeBits(count: Int, value: Int) {
        var numBits = count

        var byteIndex = bitIndex shr 3
        var bitOffset = 8 - (bitIndex and 7)
        bitIndex += numBits

        while (numBits > bitOffset) {
            val max = masks[bitOffset]
            val tmp = data[byteIndex].toInt() and max.inv() or (value shr numBits - bitOffset and max)
            data[byteIndex++] = tmp.toByte()
            numBits -= bitOffset
            bitOffset = 8
        }

        var dataValue = data[byteIndex].toInt()
        val mask = masks[numBits]
        if (numBits == bitOffset) {
            dataValue = dataValue and mask.inv() or (value and mask)
        } else {
            dataValue = dataValue and (mask shl bitOffset - numBits).inv()
            dataValue = dataValue or (value and mask shl bitOffset - numBits)
        }
        data[byteIndex] = dataValue.toByte()
    }

    companion object {
        val masks = IntArray(32)

        init {
            masks.indices.forEach { masks[it] = (1 shl it) - 1 }
        }
    }
}
