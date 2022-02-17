package com.runetopic.xlitekt.shared.buffer

import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readShort
import io.ktor.utils.io.core.readUByte
import io.ktor.utils.io.core.readUShort

/**
 * @author Jordan Abraham
 */
fun ByteReadPacket.readStringCp1252NullTerminated(): String = buildString {
    while (true) {
        val digit = readByte().toInt()
        if (digit == 0) break
        append(digit.toChar())
    }
}

fun ByteReadPacket.readStringCp1252NullCircumfixed(): String = buildString {
    if (readByte().toInt() != 0) throw IllegalArgumentException()
    return readStringCp1252NullTerminated()
}

fun ByteReadPacket.readUByteSubtract(): Int = readUByte().toInt() - 128

fun ByteReadPacket.readUShortLittleEndian(): Int = readUByte().toInt() + (readUByte().toInt() shl 8)
fun ByteReadPacket.readUShortLittleEndianSubtract(): Int = readUByteSubtract() + (readUByte().toInt() shl 8)

fun ByteReadPacket.readUMedium(): Int = (readUByte().toInt() shl 16) + readUShort().toInt()

fun ByteReadPacket.readIntV1(): Int = readUShort().toInt() + (readUByte().toInt() shl 24) + (readUByte().toInt() shl 16)
fun ByteReadPacket.readIntV2(): Int = (readUByte().toInt() shl 16) + (readUByte().toInt() shl 24) + readUShortLittleEndian()

fun ByteReadPacket.readSmart(): Int {
    return if (tryPeek() < 128) readByte().toInt() else readShort() - (Short.MAX_VALUE + 1)
}
