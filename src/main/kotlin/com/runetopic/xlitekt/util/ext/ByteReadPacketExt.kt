package com.runetopic.xlitekt.util.ext

import io.ktor.utils.io.core.ByteReadPacket

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

fun ByteReadPacket.readMedium(): Int = ((readByte().toInt() and 0xff) shl 16) + ((readByte().toInt() and 0xff) shl 8) + (readByte().toInt() and 0xff)

fun ByteReadPacket.readIntV1(): Int = ((readByte().toInt() and 0xff) shl 8) + (readByte().toInt() and 0xff) + ((readByte().toInt() and 0xff) shl 24) + ((readByte().toInt() and 0xff) shl 16)

fun ByteReadPacket.readIntV2(): Int = ((readByte().toInt() and 0xff) shl 16) + ((readByte().toInt() and 0xff) shl 24) + (readByte().toInt() and 0xff) + ((readByte().toInt() and 0xff) shl 8)
