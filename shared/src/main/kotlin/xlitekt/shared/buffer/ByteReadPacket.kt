package xlitekt.shared.buffer

import io.ktor.utils.io.core.*
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
fun ByteBuffer.readStringCp1252NullTerminated() = String(readUChars(duplicate().discardUntilDelimiter(0))).also {
    discard(1)
}

fun ByteBuffer.readStringCp1252NullCircumfixed(): String {
    if (readByte() != 0) throw IllegalArgumentException()
    return readStringCp1252NullTerminated()
}

fun ByteBuffer.readUChars(n: Int) = CharArray(n) { readUByte().toChar() }

fun ByteBuffer.readByte() = get().toInt()
fun ByteBuffer.readUByte() = get().toInt() and 0xff
fun ByteBuffer.readUByteSubtract() = readUByte() - 128 and 0xff
fun ByteBuffer.readUByteAdd() = readUByte() + 128 and 0xff
fun ByteBuffer.readUByteNegate() = -readUByte() and 0xff

fun ByteBuffer.readByteSubtract() = 128 - readByte()

fun ByteBuffer.readShort() = short.toInt()
fun ByteBuffer.readUShort() = short.toInt() and 0xffff
fun ByteBuffer.readUShortAdd() = (readUByte() shl 8) or readUByteAdd()
fun ByteBuffer.readUShortSubtract() = (readUByte() shl 8) or readUByteSubtract()
fun ByteBuffer.readUShortLittleEndian() = readUByte() or (readUByte() shl 8)
fun ByteBuffer.readUShortLittleEndianSubtract() = readUByteSubtract() or (readUByte() shl 8)
fun ByteBuffer.readUShortLittleEndianAdd() = readUByteAdd() or (readUByte() shl 8)

fun ByteBuffer.readUMedium() = (readUByte() shl 16) or readUShort()

fun ByteBuffer.readInt() = int
fun ByteBuffer.readIntLittleEndian() = (readUByte() and 0xff) or (readUByte() shl 8) or (readUByte() shl 16) or (readUByte() shl 24)
fun ByteBuffer.readIntV1() = readUShort() or (readUByte() shl 24) or (readUByte() shl 16)
fun ByteBuffer.readIntV2() = (readUByte() shl 16) or (readUByte() shl 24) or readUShortLittleEndian()

tailrec fun ByteBuffer.readIncrSmallSmart(increment: Int = readUShortSmart(), offset: Int = 0): Int {
    if (increment != Short.MAX_VALUE.toInt()) return offset + increment
    return readIncrSmallSmart(offset = offset + Short.MAX_VALUE)
}

fun ByteBuffer.readShortSmart() = if (tryPeek() < 128) readUByte() - 64 else readUShort() - 49152
fun ByteBuffer.readUShortSmart() = if (tryPeek() < 128) readUByte() else readUShort() - (Short.MAX_VALUE + 1)
fun ByteBuffer.readUIntSmart() = if (tryPeek() < 0) readInt() and Integer.MAX_VALUE else readUShort().toShort().let { if (it == Short.MAX_VALUE) -1 else it }.toInt()

tailrec fun ByteBuffer.readVarInt(increment: Int = readByte(), offset: Int = 0): Int {
    if (increment >= 0) return offset or increment
    return readVarInt(offset = (offset or (increment and 127)) shl 7)
}

fun ByteBuffer.tryPeek() = duplicate().readUByte()

fun ByteBuffer.discard(n: Int) {
    position(position() + n)
}

fun ByteBuffer.discardUntilDelimiter(delimiter: Int): Int {
    var count = 0
    while (readByte() != delimiter) {
        count++
    }
    return count
}
