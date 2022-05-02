package xlitekt.cache.provider.music

import xlitekt.cache.provider.EntryType

/**
 * @author Jordan Abraham
 */
data class MusicEntryType(
    override val id: Int,
    var name: String? = null,
    var bytes: ByteArray? = null
) : EntryType(id) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MusicEntryType

        if (id != other.id) return false
        if (name != other.name) return false
        if (bytes != null) {
            if (other.bytes == null) return false
            if (!bytes.contentEquals(other.bytes)) return false
        } else if (other.bytes != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (bytes?.contentHashCode() ?: 0)
        return result
    }
}
