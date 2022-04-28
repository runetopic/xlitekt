package xlitekt.cache.provider.texture

import kotlinx.serialization.Serializable
import xlitekt.cache.provider.EntryType

/**
 * @author Jordan Abraham
 */
@Serializable
data class TextureEntryType(
    override val id: Int,
    var averageRGB: Int = 0,
    var field2206: Boolean = false,
    var textureIds: IntArray? = null,
    var animationDirection: Int = 0,
    var animationSpeed: Int = 0
) : EntryType(id) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TextureEntryType

        if (id != other.id) return false
        if (averageRGB != other.averageRGB) return false
        if (field2206 != other.field2206) return false
        if (textureIds != null) {
            if (other.textureIds == null) return false
            if (!textureIds.contentEquals(other.textureIds)) return false
        } else if (other.textureIds != null) return false
        if (animationDirection != other.animationDirection) return false
        if (animationSpeed != other.animationSpeed) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + averageRGB
        result = 31 * result + field2206.hashCode()
        result = 31 * result + (textureIds?.contentHashCode() ?: 0)
        result = 31 * result + animationDirection
        result = 31 * result + animationSpeed
        return result
    }

}
