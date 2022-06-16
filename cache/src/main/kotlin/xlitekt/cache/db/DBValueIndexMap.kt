package xlitekt.cache.db

import xlitekt.shared.buffer.readInt
import xlitekt.shared.buffer.readLong
import xlitekt.shared.buffer.readStringCp1252NullTerminated
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
private object DBIntValue : DBValue<Int> {
    override fun read(buffer: ByteBuffer) = buffer.readInt()
}

private object DBLongValue : DBValue<Long> {
    override fun read(buffer: ByteBuffer) = buffer.readLong()
}

private object DBStringValue : DBValue<String> {
    override fun read(buffer: ByteBuffer) = buffer.readStringCp1252NullTerminated()
}

internal object DBValueIndexMap : Map<Int, DBValue<*>> by mapOf(
    0 to DBIntValue,
    1 to DBLongValue,
    2 to DBStringValue
)
