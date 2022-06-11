package xlitekt.shared.buffer

import io.ktor.utils.io.core.BytePacketBuilder
import io.ktor.utils.io.core.readBytes
import io.ktor.utils.io.core.writeShort

/**
 * @author Jordan Abraham
 *
 * Extension functions for the BytePacketBuilder class.
 * These extension functions are only used for building small byte buffers that require dynamic allocation.
 * These are not to be used for game packets.
 */
inline fun dynamicBuffer(block: BytePacketBuilder.() -> Unit): ByteArray = BytePacketBuilder()
    .also { block.invoke(it) }
    .build()
    .readBytes()

fun BytePacketBuilder.writeStringCp1252NullTerminated(value: String) {
    value.toByteArray().forEach(::writeByte)
    writeByte(0)
}

fun BytePacketBuilder.writeByteAdd(value: Int) {
    writeByte((value.toByte() + 128).toByte())
}

fun BytePacketBuilder.writeSmart(value: Int) {
    if (value > 128) writeShort(value.toShort()) else writeByte(value.toByte())
}

fun BytePacketBuilder.writeBytes(bytes: ByteArray) {
    bytes.forEach(::writeByte)
}

fun BytePacketBuilder.writeBytesAdd(bytes: ByteArray) {
    bytes.forEach { writeByteAdd(it.toInt()) }
}

fun BytePacketBuilder.writeByteNegate(value: Int) {
    writeByte((-value.toByte()).toByte())
}
