package xlitekt.shared.buffer

import io.ktor.util.moveToByteArray
import xlitekt.shared.toInt
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
inline fun allocate(limit: Int, block: ByteBuffer.() -> Unit): ByteArray = ByteBuffer
    .allocate(limit)
    .also(block)
    .rewind()
    .moveToByteArray()

inline fun allocateDynamic(limit: Int, block: ByteBuffer.() -> Unit): ByteArray = ByteBuffer
    .allocate(limit)
    .also(block)
    .run {
        limit(position())
        rewind()
        moveToByteArray()
    }

fun ByteBuffer.writeStringCp1252NullTerminated(value: String) {
    value.toByteArray().forEach(::put)
    put(0)
}

fun ByteBuffer.writeBytes(bytes: ByteArray) {
    bytes.forEach(::put)
}

fun ByteBuffer.writeBytesAdd(bytes: ByteArray) {
    bytes.forEach { writeByteAdd(it.toInt()) }
}

fun ByteBuffer.writeSmart(value: Int) {
    if (value > 128) putShort(value.toShort()) else put(value.toByte())
}

fun ByteBuffer.writeByte(value: Int) {
    put(value.toByte())
}

fun ByteBuffer.writeByteSubtract(value: Int) {
    put((128 - value.toByte()).toByte())
}

fun ByteBuffer.writeByteAdd(value: Int) {
    put((value.toByte() + 128).toByte())
}

fun ByteBuffer.writeByteNegate(value: Int) {
    put((-value.toByte()).toByte())
}

fun ByteBuffer.writeShort(value: Int) {
    putShort(value.toShort())
}

fun ByteBuffer.writeShortAdd(value: Int) {
    put((value shr 8).toByte())
    writeByteAdd(value)
}

fun ByteBuffer.writeShortLittleEndian(value: Int) {
    put(value.toByte())
    put((value shr 8).toByte())
}

fun ByteBuffer.writeShortLittleEndianAdd(value: Int) {
    writeByteAdd(value)
    put((value shr 8).toByte())
}

fun ByteBuffer.writeMedium(value: Int) {
    put((value shr 16).toByte())
    putShort(value.toShort())
}

fun ByteBuffer.writeInt(value: Int) {
    putInt(value)
}

fun ByteBuffer.writeIntLittleEndian(value: Int) {
    writeShortLittleEndian(value)
    put((value shr 16).toByte())
    put((value shr 24).toByte())
}

fun ByteBuffer.writeIntV1(value: Int) {
    putShort(value.toShort())
    put((value shr 24).toByte())
    put((value shr 16).toByte())
}

fun ByteBuffer.writeIntV2(value: Int) {
    put((value shr 16).toByte())
    put((value shr 24).toByte())
    writeShortLittleEndian(value)
}

fun ByteBuffer.fill(n: Int, value: Int) {
    repeat(n) { put(value.toByte()) }
}

inline fun ByteBuffer.withBitAccess(block: BitAccess.() -> Unit) {
    val accessor = BitAccess()
    block.invoke(accessor)
    put(accessor.data, 0, (accessor.bitIndex + 7) / 8)
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
