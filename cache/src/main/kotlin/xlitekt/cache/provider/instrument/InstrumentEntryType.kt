package xlitekt.cache.provider.instrument

import xlitekt.cache.provider.EntryType
import xlitekt.cache.provider.vorbis.VorbisEntryTypeProvider
import xlitekt.shared.inject

/**
 * @author Jordan Abraham
 */
data class InstrumentEntryType(
    override val id: Int,
    var rawSounds: Array<RawSound?>? = null,
    var field3113: ShortArray? = null,
    var field3111: ByteArray? = null,
    var field3115: ByteArray? = null,
    var field3117: Array<Instrument?>? = null,
    var field3119: ByteArray? = null,
    var field3118: IntArray? = null,
    var field3114: Int = 0
) : EntryType(id) {
    fun method5253(bytes: ByteArray?, samples: IntArray): Boolean {
        var var4 = true
        var var5 = 0
        var var6: RawSound? = null

        repeat(128) {
            if (bytes == null || bytes[it].toInt() != 0) {
                var var8 = field3118!![it]
                if (var8 != 0) {
                    if (var5 != var8) {
                        var5 = var8--
                        var6 = if (var8 and 1 == 0) {
                            null // getSoundEffect(var8 shr 2, samples)
                        } else {
                            getMusicSample(var8 shr 2, samples)
                        }
                        if (var6 == null) {
                            var4 = false
                        }
                    }

                    if (var6 != null) {
                        rawSounds!![it] = var6
                        field3118!![it] = 0
                    }
                }
            }
        }
        return var4
    }

    private fun getMusicSample(id: Int, samples: IntArray): RawSound? {
        val var7 = vorbis.entryType(id) ?: return null
        val sound = var7.toRawSound(samples) ?: return null
        raws[id] = sound
        return sound
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as InstrumentEntryType

        if (id != other.id) return false
        if (rawSounds != null) {
            if (other.rawSounds == null) return false
            if (!rawSounds.contentEquals(other.rawSounds)) return false
        } else if (other.rawSounds != null) return false
        if (field3113 != null) {
            if (other.field3113 == null) return false
            if (!field3113.contentEquals(other.field3113)) return false
        } else if (other.field3113 != null) return false
        if (field3111 != null) {
            if (other.field3111 == null) return false
            if (!field3111.contentEquals(other.field3111)) return false
        } else if (other.field3111 != null) return false
        if (field3115 != null) {
            if (other.field3115 == null) return false
            if (!field3115.contentEquals(other.field3115)) return false
        } else if (other.field3115 != null) return false
        if (field3117 != null) {
            if (other.field3117 == null) return false
            if (!field3117.contentEquals(other.field3117)) return false
        } else if (other.field3117 != null) return false
        if (field3119 != null) {
            if (other.field3119 == null) return false
            if (!field3119.contentEquals(other.field3119)) return false
        } else if (other.field3119 != null) return false
        if (field3118 != null) {
            if (other.field3118 == null) return false
            if (!field3118.contentEquals(other.field3118)) return false
        } else if (other.field3118 != null) return false
        if (field3114 != other.field3114) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + (rawSounds?.contentHashCode() ?: 0)
        result = 31 * result + (field3113?.contentHashCode() ?: 0)
        result = 31 * result + (field3111?.contentHashCode() ?: 0)
        result = 31 * result + (field3115?.contentHashCode() ?: 0)
        result = 31 * result + (field3117?.contentHashCode() ?: 0)
        result = 31 * result + (field3119?.contentHashCode() ?: 0)
        result = 31 * result + (field3118?.contentHashCode() ?: 0)
        result = 31 * result + field3114
        return result
    }

    companion object {
        val vorbis by inject<VorbisEntryTypeProvider>()
        val raws = mutableMapOf<Int, RawSound>()
    }
}

data class RawSound(
    val sampleRate: Int,
    val samples: ByteArray,
    val start: Int,
    val end: Int,
    val field265: Boolean
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RawSound

        if (sampleRate != other.sampleRate) return false
        if (!samples.contentEquals(other.samples)) return false
        if (start != other.start) return false
        if (end != other.end) return false
        if (field265 != other.field265) return false

        return true
    }

    override fun hashCode(): Int {
        var result = sampleRate
        result = 31 * result + samples.contentHashCode()
        result = 31 * result + start
        result = 31 * result + end
        result = 31 * result + field265.hashCode()
        return result
    }
}

data class Instrument(
    var field3056: ByteArray? = null,
    var field3054: ByteArray? = null,
    var field3052: Int = 0,
    var field3055: Int = 0,
    var field3053: Int = 0,
    var field3057: Int = 0,
    var field3059: Int = 0,
    var field3058: Int = 0,
    var field3060: Int = 0
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Instrument

        if (field3056 != null) {
            if (other.field3056 == null) return false
            if (!field3056.contentEquals(other.field3056)) return false
        } else if (other.field3056 != null) return false
        if (field3054 != null) {
            if (other.field3054 == null) return false
            if (!field3054.contentEquals(other.field3054)) return false
        } else if (other.field3054 != null) return false
        if (field3052 != other.field3052) return false
        if (field3055 != other.field3055) return false
        if (field3053 != other.field3053) return false
        if (field3057 != other.field3057) return false
        if (field3059 != other.field3059) return false
        if (field3058 != other.field3058) return false
        if (field3060 != other.field3060) return false

        return true
    }

    override fun hashCode(): Int {
        var result = field3056?.contentHashCode() ?: 0
        result = 31 * result + (field3054?.contentHashCode() ?: 0)
        result = 31 * result + field3052
        result = 31 * result + field3055
        result = 31 * result + field3053
        result = 31 * result + field3057
        result = 31 * result + field3059
        result = 31 * result + field3058
        result = 31 * result + field3060
        return result
    }
}
