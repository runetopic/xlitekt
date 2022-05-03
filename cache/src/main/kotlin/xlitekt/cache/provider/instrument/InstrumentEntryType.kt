package xlitekt.cache.provider.instrument

import xlitekt.cache.provider.EntryType

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
) : EntryType(id)

data class RawSound(
    val sampleRate: Int,
    val samples: ByteArray,
    val start: Int,
    val end: Int,
    val field265: Boolean
) {
    constructor(sampleRate: Int, samples: ByteArray, start: Int, end: Int) : this(sampleRate, samples, start, end, false)
}

data class Instrument(
    var field3056: ByteArray? = null,
    var field3054: ByteArray? = null
)
