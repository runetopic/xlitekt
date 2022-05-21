package xlitekt.shared.buffer

import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.discardUntilDelimiter
import io.ktor.utils.io.core.readBytes
import io.ktor.utils.io.core.readInt
import io.ktor.utils.io.core.readUByte
import io.ktor.utils.io.core.readUShort

/**
 * @author Jordan Abraham
 */
fun ByteReadPacket.readStringCp1252NullTerminated() = String(readBytes(copy().discardUntilDelimiter(0).toInt())).also {
    discard(1)
}

fun ByteReadPacket.readStringCp1252NullCircumfixed(): String {
    if (readByte().toInt() != 0) throw IllegalArgumentException()
    return readStringCp1252NullTerminated()
}

fun ByteReadPacket.readUByteSubtract() = readUByte().toInt() - 128 and 0xff
fun ByteReadPacket.readUByteAdd() = readUByte().toInt() + 128 and 0xff
fun ByteReadPacket.readUByteNegate() = -readByte().toInt() and 0xff

fun ByteReadPacket.readUShortAdd() = (readUByte().toInt() shl 8) or readUByteAdd()
fun ByteReadPacket.readUShortSubtract() = (readUByte().toInt() shl 8) or readUByteSubtract()
fun ByteReadPacket.readUShortLittleEndian() = readUByte().toInt() or (readUByte().toInt() shl 8)
fun ByteReadPacket.readUShortLittleEndianSubtract() = readUByteSubtract() or (readUByte().toInt() shl 8)
fun ByteReadPacket.readUShortLittleEndianAdd() = readUByteAdd() or (readUByte().toInt() shl 8)

fun ByteReadPacket.readUMedium() = (readUByte().toInt() shl 16) or readUShort().toInt()

fun ByteReadPacket.readIntV1() = readUShort().toInt() or (readUByte().toInt() shl 24) or (readUByte().toInt() shl 16)
fun ByteReadPacket.readIntV2() = (readUByte().toInt() shl 16) or (readUByte().toInt() shl 24) or readUShortLittleEndian()

tailrec fun ByteReadPacket.readIncrSmallSmart(increment: Int = readUShortSmart(), offset: Int = 0): Int {
    if (increment != Short.MAX_VALUE.toInt()) return offset + increment
    return readIncrSmallSmart(offset = offset + Short.MAX_VALUE)
}

fun ByteReadPacket.readShortSmart() = if (tryPeek() < 128) readUByte().toInt() - 64 else readUShort().toInt() - 49152
fun ByteReadPacket.readUShortSmart() = if (tryPeek() < 128) readUByte().toInt() else readUShort().toInt() - (Short.MAX_VALUE + 1)
fun ByteReadPacket.readUIntSmart() = if (tryPeek() < 0) readInt() and Integer.MAX_VALUE else readUShort().toShort().let { if (it == Short.MAX_VALUE) -1 else it }.toInt()

tailrec fun ByteReadPacket.readVarInt(increment: Int = readUByte().toInt(), offset: Int = 0): Int {
    if (increment >= 0) return offset or increment
    return readVarInt(offset = (offset or (increment and 127)) shl 7)
}
