package xlitekt.cache.provider.instrument

/**
 * @author Jordan Abraham
 */
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
