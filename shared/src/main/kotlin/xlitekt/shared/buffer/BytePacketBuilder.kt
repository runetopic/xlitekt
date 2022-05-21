package xlitekt.shared.buffer

import io.ktor.utils.io.core.BytePacketBuilder
import io.ktor.utils.io.core.writeFully
import io.ktor.utils.io.core.writeInt
import io.ktor.utils.io.core.writeIntLittleEndian
import io.ktor.utils.io.core.writeShort
import io.ktor.utils.io.core.writeShortLittleEndian
import xlitekt.shared.toInt

inline fun BytePacketBuilder.writeStringCp1252NullTerminated(value: () -> String) {
    value.invoke().toByteArray().forEach(::writeByte)
    writeByte { 0 }
}

inline fun BytePacketBuilder.writeBytes(bytes: () -> ByteArray) = bytes.invoke().forEach(::writeByte)
inline fun BytePacketBuilder.writeBytesAdd(bytes: () -> ByteArray) = bytes.invoke().forEach { writeByteAdd(it::toInt) }

inline fun BytePacketBuilder.writeSmart(value: () -> Int) = value.invoke().also {
    if (it > 128) writeShort { it } else writeByte { it }
}

inline fun BytePacketBuilder.writeByte(value: () -> Int) = writeByte(value.invoke().toByte())
inline fun BytePacketBuilder.writeByteNegate(value: () -> Int) = writeByte((-value.invoke().toByte()).toByte())
inline fun BytePacketBuilder.writeByteSubtract(value: () -> Int) = writeByte((128 - value.invoke().toByte()).toByte())
inline fun BytePacketBuilder.writeByteAdd(value: () -> Int) = writeByte((value.invoke().toByte() + 128).toByte())

inline fun BytePacketBuilder.writeShort(value: () -> Int) = writeShort(value.invoke().toShort())
inline fun BytePacketBuilder.writeShortLittleEndian(value: () -> Int) = writeShortLittleEndian(value.invoke().toShort())

inline fun BytePacketBuilder.writeShortAdd(value: () -> Int) = value.invoke().also {
    writeByte { it shr 8 }
    writeByteAdd { it }
}

inline fun BytePacketBuilder.writeShortLittleEndianAdd(value: () -> Int) = value.invoke().also {
    writeByteAdd { it }
    writeByte { it shr 8 }
}

inline fun BytePacketBuilder.writeMedium(value: () -> Int) = value.invoke().also {
    writeByte { it shr 16 }
    writeShort { it }
}

inline fun BytePacketBuilder.writeInt(value: () -> Int) = writeInt(value.invoke())
inline fun BytePacketBuilder.writeIntLittleEndian(value: () -> Int) = writeIntLittleEndian(value.invoke())

inline fun BytePacketBuilder.writeIntV1(value: () -> Int) = value.invoke().also {
    writeShort { it }
    writeByte { it shr 24 }
    writeByte { it shr 16 }
}

inline fun BytePacketBuilder.writeIntV2(value: () -> Int) = value.invoke().also {
    writeByte { it shr 16 }
    writeByte { it shr 24 }
    writeShortLittleEndian { it }
}

inline fun BytePacketBuilder.withBitAccess(block: BitAccess.() -> Unit) {
    val accessor = BitAccess()
    block.invoke(accessor)
    writeFully(accessor.data, 0, (accessor.bitIndex + 7) / 8)
}

class BitAccess {
    var bitIndex = 0
    val data = ByteArray(4096 * 2)

    inline fun writeBit(value: () -> Boolean) = value.invoke().also {
        writeBits(1, it::toInt)
    }

    inline fun writeBits(count: Int, value: () -> Int) = value.invoke().also {
        var numBits = count

        var byteIndex = bitIndex shr 3
        var bitOffset = 8 - (bitIndex and 7)
        bitIndex += numBits

        while (numBits > bitOffset) {
            val max = masks[bitOffset]
            val tmp = data[byteIndex].toInt() and max.inv() or (it shr numBits - bitOffset and max)
            data[byteIndex++] = tmp.toByte()
            numBits -= bitOffset
            bitOffset = 8
        }

        var dataValue = data[byteIndex].toInt()
        val mask = masks[numBits]
        if (numBits == bitOffset) {
            dataValue = dataValue and mask.inv() or (it and mask)
        } else {
            dataValue = dataValue and (mask shl bitOffset - numBits).inv()
            dataValue = dataValue or (it and mask shl bitOffset - numBits)
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
