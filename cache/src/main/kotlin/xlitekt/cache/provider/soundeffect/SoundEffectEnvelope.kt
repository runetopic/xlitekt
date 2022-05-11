package xlitekt.cache.provider.soundeffect

import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readInt
import io.ktor.utils.io.core.readUByte
import io.ktor.utils.io.core.readUShort

/**
 * @author Jordan Abraham
 */
data class SoundEffectEnvelope(
    val buffer: ByteReadPacket? = null,
    var form: Int = 0,
    var start: Int = 0,
    var end: Int = 0,
    var segments: Int = 2,
    var durations: IntArray = IntArray(2),
    var phases: IntArray = IntArray(2)
) {
    var ticks = 0
    var phaseIndex = 0
    var step = 0
    var amplitude = 0
    var max = 0

    init {
        durations[1] = 65535
        phases[1] = 65535
        if (buffer != null) {
            form = buffer.readUByte().toInt()
            start = buffer.readInt()
            end = buffer.readInt()
            decodeSegments(buffer)
        }
    }

    fun decodeSegments(buffer: ByteReadPacket) {
        segments = buffer.readUByte().toInt()
        durations = IntArray(segments)
        phases = IntArray(segments)
        repeat(segments) {
            durations[it] = buffer.readUShort().toInt()
            phases[it] = buffer.readUShort().toInt()
        }
    }

    fun reset() {
        ticks = 0
        phaseIndex = 0
        step = 0
        amplitude = 0
        max = 0
    }

    fun doStep(var1: Int): Int {
        if (max >= ticks) {
            amplitude = phases[phaseIndex++] shl 15
            if (phaseIndex >= segments) {
                phaseIndex = segments - 1
            }
            ticks = (durations[phaseIndex].toDouble() / 65536.0 * var1.toDouble()).toInt()
            if (ticks > max) {
                step = ((phases[phaseIndex] shl 15) - amplitude) / (ticks - max)
            }
        }
        amplitude += step
        ++max
        return amplitude - step shr 15
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SoundEffectEnvelope

        if (buffer != other.buffer) return false
        if (form != other.form) return false
        if (start != other.start) return false
        if (end != other.end) return false
        if (segments != other.segments) return false
        if (!durations.contentEquals(other.durations)) return false
        if (!phases.contentEquals(other.phases)) return false
        if (ticks != other.ticks) return false
        if (phaseIndex != other.phaseIndex) return false
        if (step != other.step) return false
        if (amplitude != other.amplitude) return false
        if (max != other.max) return false

        return true
    }

    override fun hashCode(): Int {
        var result = buffer?.hashCode() ?: 0
        result = 31 * result + form
        result = 31 * result + start
        result = 31 * result + end
        result = 31 * result + segments
        result = 31 * result + durations.contentHashCode()
        result = 31 * result + phases.contentHashCode()
        result = 31 * result + ticks
        result = 31 * result + phaseIndex
        result = 31 * result + step
        result = 31 * result + amplitude
        result = 31 * result + max
        return result
    }
}
