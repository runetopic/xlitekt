package com.runetopic.xlitekt.shared.buffer

import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readInt
import io.ktor.utils.io.core.readUByte
import io.ktor.utils.io.core.readUShort

/**
 * @author Jordan Abraham
 */
fun ByteReadPacket.readStringCp1252NullTerminated() = buildString {
    while (true) {
        val digit = readByte().toInt()
        if (digit == 0) break
        append(digit.toChar())
    }
}

fun ByteReadPacket.readStringCp1252NullCircumfixed() = buildString {
    if (readByte().toInt() != 0) throw IllegalArgumentException()
    return readStringCp1252NullTerminated()
}

fun ByteReadPacket.readUByteSubtract() = readUByte().toInt() - 128

fun ByteReadPacket.readUShortLittleEndian() = readUByte().toInt() + (readUByte().toInt() shl 8)
fun ByteReadPacket.readUShortLittleEndianSubtract() = readUByteSubtract() + (readUByte().toInt() shl 8)

fun ByteReadPacket.readUMedium() = (readUByte().toInt() shl 16) + readUShort().toInt()

fun ByteReadPacket.readIntV1() = readUShort().toInt() + (readUByte().toInt() shl 24) + (readUByte().toInt() shl 16)
fun ByteReadPacket.readIntV2() = (readUByte().toInt() shl 16) + (readUByte().toInt() shl 24) + readUShortLittleEndian()

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

fun ByteReadPacket.readUShortSmart() = if (tryPeek() < 128) readUByte().toInt() else readUShort().toInt() - (Short.MAX_VALUE + 1)
fun ByteReadPacket.readUIntSmart() = if (tryPeek() < 0) readInt() and Integer.MAX_VALUE else readUShort().toShort().let { if (it == Short.MAX_VALUE) -1 else it }.toInt()

