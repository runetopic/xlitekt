package xlitekt.shared.buffer

import com.runetopic.cryptography.isaac.ISAAC
import io.ktor.utils.io.ByteReadChannel

/**
 * @author Jordan Abraham
 * Extension functions for the ByteReadChannel class.
 * Extension functions for unpacking packet data directly from the ByteReadChannel.
 */
suspend fun ByteReadChannel.readStringCp1252NullTerminated() = buildString {
    var char: Char
    while (readByte().toInt().also { char = it.toChar() } != 0) append(char)
}

suspend fun ByteReadChannel.readUByte() = readByte().toInt() and 0xff
suspend fun ByteReadChannel.readUByteSubtract() = readUByte() - 128 and 0xff
suspend fun ByteReadChannel.readUByteAdd() = readUByte() + 128 and 0xff
suspend fun ByteReadChannel.readUByteNegate() = -readUByte() and 0xff

suspend fun ByteReadChannel.readUShort() = readShort().toInt() and 0xffff
suspend fun ByteReadChannel.readUShortAdd() = (readUByte() shl 8) or readUByteAdd()
suspend fun ByteReadChannel.readUShortSubtract() = (readUByte() shl 8) or readUByteSubtract()
suspend fun ByteReadChannel.readUShortLittleEndian() = readUByte() or (readUByte() shl 8)
suspend fun ByteReadChannel.readUShortLittleEndianSubtract() = readUByteSubtract() or (readUByte() shl 8)
suspend fun ByteReadChannel.readUShortLittleEndianAdd() = readUByteAdd() or (readUByte() shl 8)

suspend fun ByteReadChannel.readUMedium() = (readUByte() shl 16) or readUShort()

suspend fun ByteReadChannel.readIntLittleEndian() = readUShortLittleEndian() or (readUByte() shl 16) or (readUByte() shl 24)
suspend fun ByteReadChannel.readIntV1() = readUShort() or (readUByte() shl 24) or (readUByte() shl 16)
suspend fun ByteReadChannel.readIntV2() = (readUByte() shl 16) or (readUByte() shl 24) or readUShortLittleEndian()

suspend fun ByteReadChannel.readUShortSmart(): Int {
    val peek = readUByte()
    return if (peek < 128) peek else (peek shl 8 or readUByte()) - 32768
}

suspend fun ByteReadChannel.readPacketOpcode(isaac: ISAAC) = (readUByte() - isaac.getNext() and 0xff).let {
    if (it > Byte.MAX_VALUE) (it - (Byte.MAX_VALUE + 1) shl 8) or (readUByte() - isaac.getNext()) else it
}

suspend fun ByteReadChannel.readPacketSize(size: Int) = if (size == -1 || size == -2) {
    if (size == -1) readUByte() else readUShort()
} else size
