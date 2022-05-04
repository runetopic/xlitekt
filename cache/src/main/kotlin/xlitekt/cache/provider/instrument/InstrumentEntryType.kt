package xlitekt.cache.provider.instrument

import xlitekt.cache.provider.EntryType
import xlitekt.cache.provider.vorbis.VorbisEntryTypeProvider
import xlitekt.shared.inject

/**
 * @author Jordan Abraham
 */
data class InstrumentEntryType(
    override val id: Int,
    var instrumentSamples: Array<InstrumentSample?>? = null,
    var field3113: ShortArray? = null,
    var field3111: ByteArray? = null,
    var field3115: ByteArray? = null,
    var field3117: Array<Instrument?>? = null,
    var field3119: ByteArray? = null,
    var groupIdOffsets: IntArray? = null,
    var field3114: Int = 0
) : EntryType(id) {
    fun loadVorbisSamples(bytes: ByteArray?, samples: IntArray): Boolean {
        var loaded = true
        var offset = 0
        repeat(128) {
            if (bytes == null || bytes[it].toInt() != 0) {
                var idOffset = groupIdOffsets!![it]
                if (idOffset != 0) {
                    var instrumentSample: InstrumentSample? = null
                    if (offset != idOffset) {
                        offset = idOffset--
                        instrumentSample = if (idOffset and 1 == 0) {
                            null // getSoundEffect(var8 shr 2, samples)
                        } else {
                            getSampleForTrack(idOffset shr 2, samples)
                        }
                        if (instrumentSample == null) {
                            loaded = false
                        }
                    }

                    if (instrumentSample != null) {
                        instrumentSamples!![it] = instrumentSample
                        groupIdOffsets!![it] = 0
                    }
                }
            }
        }
        return loaded
    }

    private fun getSampleForTrack(id: Int, samples: IntArray): InstrumentSample? {
        val entry = vorbis.entryType(id) ?: return null
        val sample = entry.toInstrumentSample(samples) ?: return null
        globalInstrumentSamples[id] = sample
        return sample
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as InstrumentEntryType

        if (id != other.id) return false
        if (instrumentSamples != null) {
            if (other.instrumentSamples == null) return false
            if (!instrumentSamples.contentEquals(other.instrumentSamples)) return false
        } else if (other.instrumentSamples != null) return false
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
        if (groupIdOffsets != null) {
            if (other.groupIdOffsets == null) return false
            if (!groupIdOffsets.contentEquals(other.groupIdOffsets)) return false
        } else if (other.groupIdOffsets != null) return false
        if (field3114 != other.field3114) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + (instrumentSamples?.contentHashCode() ?: 0)
        result = 31 * result + (field3113?.contentHashCode() ?: 0)
        result = 31 * result + (field3111?.contentHashCode() ?: 0)
        result = 31 * result + (field3115?.contentHashCode() ?: 0)
        result = 31 * result + (field3117?.contentHashCode() ?: 0)
        result = 31 * result + (field3119?.contentHashCode() ?: 0)
        result = 31 * result + (groupIdOffsets?.contentHashCode() ?: 0)
        result = 31 * result + field3114
        return result
    }

    companion object {
        val vorbis by inject<VorbisEntryTypeProvider>()
        val globalInstrumentSamples = mutableMapOf<Int, InstrumentSample>()
    }
}
