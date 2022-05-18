package xlitekt.shared.buffer

import io.ktor.utils.io.core.BytePacketBuilder
import io.ktor.utils.io.core.writeFully
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

inline fun BytePacketBuilder.writeShort(value: () -> Int) = value.invoke().also {
    writeByte { it shr 8 }
    writeByte { it }
}

inline fun BytePacketBuilder.writeShortLittleEndian(value: () -> Int) = value.invoke().also {
    writeByte { it }
    writeByte { it shr 8 }
}

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

inline fun BytePacketBuilder.writeInt(value: () -> Int) = value.invoke().also {
    writeByte { it shr 24 }
    writeByte { it shr 16 }
    writeShort { it }
}

inline fun BytePacketBuilder.writeIntLittleEndian(value: () -> Int) = value.invoke().also {
    writeShortLittleEndian { it }
    writeByte { it shr 16 }
    writeByte { it shr 24 }
}

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

        var tmp: Int
        var max: Int
        while (numBits > bitOffset) {
            tmp = data[byteIndex].toInt()
            max = masks[bitOffset]
            tmp = tmp and max.inv() or (it shr numBits - bitOffset and max)
            data[byteIndex++] = tmp.toByte()
            numBits -= bitOffset
            bitOffset = 8
        }

        tmp = data[byteIndex].toInt()
        max = masks[numBits]
        if (numBits == bitOffset) {
            tmp = tmp and max.inv() or (it and max)
        } else {
            tmp = tmp and (max shl bitOffset - numBits).inv()
            tmp = tmp or (it and max shl bitOffset - numBits)
        }
        data[byteIndex] = tmp.toByte()
    }

    fun write(builder: BytePacketBuilder) = builder.writeFully(data, 0, (bitIndex + 7) / 8)

    companion object {
        val masks = IntArray(32)

        init {
            masks.indices.forEach { masks[it] = (1 shl it) - 1 }
        }
    }
}
