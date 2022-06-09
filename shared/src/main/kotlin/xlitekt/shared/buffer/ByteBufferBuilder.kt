package xlitekt.shared.buffer

import xlitekt.shared.toInt
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
inline fun allocate(limit: Int, block: ByteBuffer.() -> Unit): ByteArray = ByteBuffer.allocate(limit).also { block.invoke(it) }.array()
inline fun allocateDynamic(limit: Int, block: ByteBuffer.() -> Unit): ByteArray {
    val pool = ByteBuffer.allocate(limit)
    block.invoke(pool)
    return ByteBuffer.allocate(pool.position()).put(pool.array(), 0, pool.position()).array()
}

inline fun ByteBuffer.writeStringCp1252NullTerminated(value: () -> String) {
    value.invoke().toByteArray().forEach(::put)
    put(0)
}

inline fun ByteBuffer.writeBytes(bytes: () -> ByteArray) = bytes.invoke().forEach(::put)
inline fun ByteBuffer.writeBytesAdd(bytes: () -> ByteArray) = bytes.invoke().forEach { writeByteAdd(it::toInt) }

inline fun ByteBuffer.writeSmart(value: () -> Int) = value.invoke().also {
    if (it > 128) putShort(it.toShort()) else put(it.toByte())
}

inline fun ByteBuffer.writeByte(value: () -> Int) = value.invoke().toByte().also(::put)

inline fun ByteBuffer.writeByteSubtract(value: () -> Int) = value.invoke().toByte().also {
    put((128 - it).toByte())
}

inline fun ByteBuffer.writeByteAdd(value: () -> Int) = value.invoke().toByte().also {
    put((it + 128).toByte())
}

inline fun ByteBuffer.writeByteNegate(value: () -> Int) = value.invoke().toByte().also {
    put((-it).toByte())
}

inline fun ByteBuffer.writeShort(value: () -> Int) = value.invoke().toShort().also(::putShort)

inline fun ByteBuffer.writeShortAdd(value: () -> Int) = value.invoke().also {
    put((it shr 8).toByte())
    writeByteAdd { it }
}

inline fun ByteBuffer.writeShortLittleEndian(value: () -> Int) = value.invoke().also {
    put(it.toByte())
    put((it shr 8).toByte())
}

inline fun ByteBuffer.writeShortLittleEndianAdd(value: () -> Int) = value.invoke().also {
    writeByteAdd { it }
    put((it shr 8).toByte())
}

inline fun ByteBuffer.writeMedium(value: () -> Int) = value.invoke().also {
    put((it shr 16).toByte())
    putShort(it.toShort())
}

inline fun ByteBuffer.writeInt(value: () -> Int) = value.invoke().also(::putInt)

inline fun ByteBuffer.writeIntLittleEndian(value: () -> Int) = value.invoke().also {
    put(it.toByte())
    put((it shr 8).toByte())
    put((it shr 16).toByte())
    put((it shr 24).toByte())
}

inline fun ByteBuffer.writeIntV1(value: () -> Int) = value.invoke().also {
    putShort(it.toShort())
    put((it shr 24).toByte())
    put((it shr 16).toByte())
}

inline fun ByteBuffer.writeIntV2(value: () -> Int) = value.invoke().also {
    put((it shr 16).toByte())
    put((it shr 24).toByte())
    writeShortLittleEndian { it }
}

inline fun ByteBuffer.fill(n: Int, value: () -> Int) = value.invoke().also {
    repeat(n) {
        writeByte { it }
    }
}

inline fun ByteBuffer.withBitAccess(block: BitAccess.() -> Unit) {
    val accessor = BitAccess()
    block.invoke(accessor)
    put(accessor.data, 0, (accessor.bitIndex + 7) / 8)
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
