package xlitekt.cache.provider.vorbis

import xlitekt.cache.provider.vorbis.VorbisEntryTypeProvider.Companion.readBit
import xlitekt.cache.provider.vorbis.VorbisEntryTypeProvider.Companion.readBits

/**
 * @author Jordan Abraham
 */
internal data class VorbisMapping(
    var submaps: Int = 0,
    var mappingMux: Int = 0,
    var submapFloor: IntArray? = null,
    var submapResidue: IntArray? = null
) {
    init {
        readBits(16)
        submaps = if (readBit() != 0) readBits(4) + 1 else 1
        if (readBit() != 0) {
            readBits(8)
        }
        readBits(2)
        if (submaps > 1) {
            mappingMux = readBits(4)
        }
        submapFloor = IntArray(submaps)
        submapResidue = IntArray(submaps)
        repeat(submaps) {
            readBits(8)
            submapFloor!![it] = readBits(8)
            submapResidue!![it] = readBits(8)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VorbisMapping

        if (submaps != other.submaps) return false
        if (mappingMux != other.mappingMux) return false
        if (submapFloor != null) {
            if (other.submapFloor == null) return false
            if (!submapFloor.contentEquals(other.submapFloor)) return false
        } else if (other.submapFloor != null) return false
        if (submapResidue != null) {
            if (other.submapResidue == null) return false
            if (!submapResidue.contentEquals(other.submapResidue)) return false
        } else if (other.submapResidue != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = submaps
        result = 31 * result + mappingMux
        result = 31 * result + (submapFloor?.contentHashCode() ?: 0)
        result = 31 * result + (submapResidue?.contentHashCode() ?: 0)
        return result
    }
}
