package com.runetopic.xlitekt.util.ext

import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readUByte

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

fun ByteReadPacket.readMedium(): Int = (readUByte().toInt() shl 16) + (readUByte().toInt() shl 8) + readUByte().toInt()

fun ByteReadPacket.readIntV1(): Int = (readUByte().toInt() shl 8) + readUByte().toInt() + (readUByte().toInt() shl 24) + (readUByte().toInt() shl 16)
fun ByteReadPacket.readIntV2(): Int = (readUByte().toInt() shl 16) + (readUByte().toInt() shl 24) + readUByte().toInt() + (readUByte().toInt() shl 8)

fun ByteReadPacket.readUByteAdd(): Int = readUByte().toInt() - 128

fun ByteReadPacket.readUShortLittleEndianAdd(): Int = readUByteAdd() + (readUByte().toInt() shl 8)
