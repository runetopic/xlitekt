package xlitekt.cache.provider.instrument

/**
 * @author Jordan Abraham
 */
data class InstrumentSample(
    val sampleRate: Int,
    val samples: ByteArray,
    val start: Int,
    val end: Int,
    val field265: Boolean
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as InstrumentSample

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
