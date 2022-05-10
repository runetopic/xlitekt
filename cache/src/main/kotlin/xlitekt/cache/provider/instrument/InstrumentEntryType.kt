package xlitekt.cache.provider.instrument

import xlitekt.cache.provider.EntryType

/**
 * @author Jordan Abraham
 */
data class InstrumentEntryType(
    override val id: Int,
    var audioBuffers: Array<InstrumentSample?>? = null,
    var pitchOffset: ShortArray? = null,
    var volumeOffset: ByteArray? = null,
    var panOffset: ByteArray? = null,
    var field3117: Array<Instrument?>? = null,
    var loopMode: ByteArray? = null,
    var offsets: IntArray? = null,
    var baseVelocity: Int = 0
) : EntryType(id) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as InstrumentEntryType

        if (id != other.id) return false
        if (audioBuffers != null) {
            if (other.audioBuffers == null) return false
            if (!audioBuffers.contentEquals(other.audioBuffers)) return false
        } else if (other.audioBuffers != null) return false
        if (pitchOffset != null) {
            if (other.pitchOffset == null) return false
            if (!pitchOffset.contentEquals(other.pitchOffset)) return false
        } else if (other.pitchOffset != null) return false
        if (volumeOffset != null) {
            if (other.volumeOffset == null) return false
            if (!volumeOffset.contentEquals(other.volumeOffset)) return false
        } else if (other.volumeOffset != null) return false
        if (panOffset != null) {
            if (other.panOffset == null) return false
            if (!panOffset.contentEquals(other.panOffset)) return false
        } else if (other.panOffset != null) return false
        if (field3117 != null) {
            if (other.field3117 == null) return false
            if (!field3117.contentEquals(other.field3117)) return false
        } else if (other.field3117 != null) return false
        if (loopMode != null) {
            if (other.loopMode == null) return false
            if (!loopMode.contentEquals(other.loopMode)) return false
        } else if (other.loopMode != null) return false
        if (offsets != null) {
            if (other.offsets == null) return false
            if (!offsets.contentEquals(other.offsets)) return false
        } else if (other.offsets != null) return false
        if (baseVelocity != other.baseVelocity) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + (audioBuffers?.contentHashCode() ?: 0)
        result = 31 * result + (pitchOffset?.contentHashCode() ?: 0)
        result = 31 * result + (volumeOffset?.contentHashCode() ?: 0)
        result = 31 * result + (panOffset?.contentHashCode() ?: 0)
        result = 31 * result + (field3117?.contentHashCode() ?: 0)
        result = 31 * result + (loopMode?.contentHashCode() ?: 0)
        result = 31 * result + (offsets?.contentHashCode() ?: 0)
        result = 31 * result + baseVelocity
        return result
    }
}
