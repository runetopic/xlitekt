package xlitekt.shared.buffer

import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readInt
import io.ktor.utils.io.core.readUByte
import io.ktor.utils.io.core.readUShort

/**
 * @author Jordan Abraham
 */
fun ByteReadPacket.readStringCp1252NullTerminated() = buildString {
    var char: Char
    while (readByte().toInt().also { char = it.toChar() } != 0) append(char)
}

fun ByteReadPacket.readStringCp1252NullCircumfixed() = buildString {
    if (readByte().toInt() != 0) throw IllegalArgumentException()
    return readStringCp1252NullTerminated()
}

fun ByteReadPacket.readUByteSubtract() = readUByte().toInt() - 128 and 0xff
fun ByteReadPacket.readUByteAdd() = readUByte().toInt() + 128 and 0xff
fun ByteReadPacket.readUByteNegate() = 0 - readByte().toInt() and 0xff

fun ByteReadPacket.readUShortAdd() = (readUByte().toInt() shl 8) or readUByteAdd()
fun ByteReadPacket.readUShortSubtract() = (readUByte().toInt() shl 8) or readUByteSubtract()
fun ByteReadPacket.readUShortLittleEndian() = readUByte().toInt() or (readUByte().toInt() shl 8)
fun ByteReadPacket.readUShortLittleEndianSubtract() = readUByteSubtract() or (readUByte().toInt() shl 8)
fun ByteReadPacket.readUShortLittleEndianAdd() = readUByteAdd() or (readUByte().toInt() shl 8)

fun ByteReadPacket.readUMedium() = (readUByte().toInt() shl 16) or readUShort().toInt()

fun ByteReadPacket.readIntV1() = readUShort().toInt() or (readUByte().toInt() shl 24) or (readUByte().toInt() shl 16)
fun ByteReadPacket.readIntV2() = (readUByte().toInt() shl 16) or (readUByte().toInt() shl 24) or readUShortLittleEndian()

fun ByteReadPacket.readIncrSmallSmart(): Int {
    var offset = 0
    var increment: Int
    increment = readUShortSmart()
    while (increment == Short.MAX_VALUE.toInt()) {
        offset += Short.MAX_VALUE.toInt()
        increment = readUShortSmart()
    }
    offset += increment
    return offset
}

fun ByteReadPacket.readShortSmart() = if (tryPeek() < 128) readUByte().toInt() - 64 else readUShort().toInt() - 49152
fun ByteReadPacket.readUShortSmart() = if (tryPeek() < 128) readUByte().toInt() else readUShort().toInt() - (Short.MAX_VALUE + 1)
fun ByteReadPacket.readUIntSmart() = if (tryPeek() < 0) readInt() and Integer.MAX_VALUE else readUShort().toShort().let { if (it == Short.MAX_VALUE) -1 else it }.toInt()

fun ByteReadPacket.readVarInt(): Int {
    var increment = readByte().toInt()
    var offset = 0
    while (increment < 0) {
        offset = (offset or (increment and 127)) shl 7
        increment = readByte().toInt()
    }
    return offset or increment
}
