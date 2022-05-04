package xlitekt.cache.provider.music

import io.ktor.utils.io.pool.ByteBufferPool
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
class MidiFileReader(
    bytes: ByteArray
) {
    private val buffer = ByteBufferPool(bytes.size, bytes.size).borrow()
    private var division = 0
    private var field3103 = 0
    private var trackStarts: IntArray? = null
    private var field3098 = 0L
    private var trackPositions: IntArray? = null
    var trackLengths: IntArray? = null
    private var field3100: IntArray? = null
    private var done: Boolean = false

    init {
        buffer.put(bytes)
        buffer.position(10)
        val size = buffer.short.toInt() and 0xffff
        division = buffer.short.toInt() and 0xffff
        field3103 = 500000
        trackStarts = IntArray(size)
        repeat(size) {
            val var4 = buffer.int
            val offset = buffer.int
            if (var4 == 1297379947) {
                trackStarts!![it] = buffer.position()
            }
            buffer.position(buffer.position() + offset)
        }
        field3098 = 0L
        trackPositions = IntArray(size)
        repeat(size) {
            trackPositions!![it] = trackStarts!![it]
        }
        trackLengths = IntArray(size)
        field3100 = IntArray(size)
    }

    fun trackCount() = trackPositions!!.size

    fun goToTrack(id: Int) {
        buffer.position(trackPositions!![id])
    }

    fun readTrackLength(id: Int) {
        trackLengths!![id] += buffer.readVarInt()
    }

    fun markTrackPosition(id: Int) {
        trackPositions!![id] = buffer.position()
    }

    fun getPrioritizedTrack(): Int {
        var trackId = -1
        var maxLength = Int.MAX_VALUE
        repeat(trackPositions!!.size) {
            if (trackPositions!![it] >= 0 && trackLengths!![it] < maxLength) {
                trackId = it
                maxLength = trackLengths!![it]
            }
        }
        return trackId
    }

    fun isDone(): Boolean {
        if (done) return true
        repeat(trackPositions!!.size) {
            if (trackPositions!![it] >= 0) return false
        }
        return true
    }

    fun setTrackDone() {
        buffer.position(0)
        done = true
    }

    fun readMessage(var1: Int): Int {
        return readMessage0(var1)
    }

    private fun readMessage0(var1: Int): Int {
        val value = buffer.array()[buffer.position()]
        val var5: Int
        if (value < 0) {
            var5 = value.toInt() and 0xff
            field3100!![var1] = var5
            buffer.position(buffer.position() + 1)
        } else {
            var5 = field3100!![var1]
        }

        if (var5 != 240 && var5 != 247) {
            return method5208(var1, var5)
        } else {
            val offset = buffer.readVarInt()
            if (var5 == 247 && offset > 0) {
                val var4 = buffer.array()[buffer.position()].toInt() and 255
                if (var4 in 241..243 || var4 == 246 || var4 == 248 || var4 in 250..252 || var4 == 254) {
                    buffer.position(buffer.position() + 1)
                    field3100!![var1] = var4
                    return method5208(var1, var4)
                }
            }
            buffer.position(buffer.position() + offset)
            return 0
        }
    }

    private fun method5208(var1: Int, var2: Int): Int {
        return if (var2 == 255) {
            val var7 = buffer.get().toInt() and 0xff
            val offset = buffer.readVarInt()
            when (var7) {
                47 -> {
                    buffer.position(buffer.position() + offset)
                    1
                }
                81 -> {
                    val var5: Int = buffer.readMedium()
                    val var6 = trackLengths!![var1]
                    field3098 += var6.toLong() * (field3103 - var5).toLong()
                    field3103 = var5
                    buffer.position(buffer.position() + (offset - 3))
                    2
                }
                else -> {
                    buffer.position(buffer.position() + offset)
                    3
                }
            }
        } else {
            val var3 = field3106[var2 - 128]
            var var4 = var2
            if (var3 >= 1) {
                var4 = var2 or ((buffer.get().toInt() and 0xff) shl 8)
            }
            if (var3 >= 2) {
                var4 = var4 or ((buffer.get().toInt() and 0xff) shl 16)
            }
            var4
        }
    }

    private fun ByteBuffer.readVarInt(): Int {
        var var1 = get().toInt()
        var var2 = 0
        while (var1 < 0) {
            var2 = (var2 or (var1 and 127)) shl 7
            var1 = get().toInt()
        }
        return var2 or var1
    }

    private fun ByteBuffer.readMedium(): Int {
        return ((get().toInt() and 0xff) shl 16) + ((get().toInt() and 0xff) shl 8) + get().toInt() and 0xff
    }

    private companion object {
        val field3106 = byteArrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0, 1, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    }
}
