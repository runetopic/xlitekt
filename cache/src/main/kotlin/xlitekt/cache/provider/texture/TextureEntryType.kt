package xlitekt.cache.provider.texture

import kotlinx.serialization.Serializable
import xlitekt.cache.provider.EntryType

/**
 * @author Jordan Abraham
 */
@Serializable
data class TextureEntryType(
    override val id: Int,
    var textureIds: IntArray? = null,
    var animationDirection: Int = 0,
    var animationSpeed: Int = 0
) : EntryType(id) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TextureEntryType

        if (id != other.id) return false
        if (textureIds != null) {
            if (other.textureIds == null) return false
            if (!textureIds.contentEquals(other.textureIds)) return false
        } else if (other.textureIds != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + (textureIds?.contentHashCode() ?: 0)
        return result
    }
}
