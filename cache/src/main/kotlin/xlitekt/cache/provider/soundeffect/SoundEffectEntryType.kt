package xlitekt.cache.provider.soundeffect

import xlitekt.cache.provider.EntryType
import xlitekt.cache.provider.instrument.InstrumentSample

/**
 * @author Jordan Abraham
 */
data class SoundEffectEntryType(
    override val id: Int,
    var instruments: Array<SoundEffectInstrument?>? = null,
    var start: Int = 0,
    var end: Int = 0
) : EntryType(id) {
    fun toInstrumentSample(samples: IntArray): InstrumentSample = InstrumentSample(samples[0], mix(), start * 22050 / 1000, end * 22050 / 1000, false)

    private fun mix(): ByteArray {
        var var1 = 0
        repeat(10) {
            if (instruments!![it] != null && instruments!![it]!!.duration + instruments!![it]!!.offset > var1) {
                var1 = instruments!![it]!!.duration + instruments!![it]!!.offset
            }
        }
        if (var1 == 0) return byteArrayOf(0)
        val var2 = var1 * 22050 / 1000
        val var3 = ByteArray(var2)
        repeat(10) {
            if (instruments!![it] != null) {
                val var5 = instruments!![it]!!.duration * 22050 / 1000
                val var6 = instruments!![it]!!.offset * 22050 / 1000
                val var7: IntArray = instruments!![it]!!.synthesize(var5, instruments!![it]!!.duration)
                repeat(var5) { var8 ->
                    var var9 = (var7[var8] shr 8) + var3[var8 + var6]
                    if (var9 + 128 and -256 != 0) {
                        var9 = var9 shr 31 xor 127
                    }
                    var3[var8 + var6] = var9.toByte()
                }
            }
        }
        return var3
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SoundEffectEntryType

        if (id != other.id) return false
        if (instruments != null) {
            if (other.instruments == null) return false
            if (!instruments.contentEquals(other.instruments)) return false
        } else if (other.instruments != null) return false
        if (start != other.start) return false
        if (end != other.end) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + (instruments?.contentHashCode() ?: 0)
        result = 31 * result + start
        result = 31 * result + end
        return result
    }
}
