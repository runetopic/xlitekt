package xlitekt.cache.provider.config.dbrow

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import xlitekt.cache.provider.EntryType

/**
 * @author Jordan Abraham
 */
@Serializable
data class DBRowEntryType(
    override val id: Int,
    var field4678: Int = 0,
    var field4676: Array<Array<@Contextual Any?>?>? = null,
    var field4677: Array<IntArray?>? = null
) : EntryType(id) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DBRowEntryType

        if (id != other.id) return false
        if (field4678 != other.field4678) return false
        if (field4676 != null) {
            if (other.field4676 == null) return false
            if (!field4676.contentDeepEquals(other.field4676)) return false
        } else if (other.field4676 != null) return false
        if (field4677 != null) {
            if (other.field4677 == null) return false
            if (!field4677.contentDeepEquals(other.field4677)) return false
        } else if (other.field4677 != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + field4678
        result = 31 * result + (field4676?.contentDeepHashCode() ?: 0)
        result = 31 * result + (field4677?.contentDeepHashCode() ?: 0)
        return result
    }
}
