package com.runetopic.xlitekt.util.ext

import io.ktor.utils.io.core.ByteReadPacket
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

fun ByteReadPacket.readMedium(): Int = (readUByte().toInt() shl 16) + (readUByte().toInt() shl 8) + readUByte().toInt()

fun ByteReadPacket.readIntV1(): Int = readUShort().toInt() + (readUByte().toInt() shl 24) + (readUByte().toInt() shl 16)
fun ByteReadPacket.readIntV2(): Int = (readUByte().toInt() shl 16) + (readUByte().toInt() shl 24) + readUShortLittleEndian()
