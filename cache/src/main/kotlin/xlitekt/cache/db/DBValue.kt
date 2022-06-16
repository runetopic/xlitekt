package xlitekt.cache.db

import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
interface DBValue<out T> {
    fun read(buffer: ByteBuffer): T
}
