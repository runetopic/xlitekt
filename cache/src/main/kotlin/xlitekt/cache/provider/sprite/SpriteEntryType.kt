package xlitekt.cache.provider.sprite

import xlitekt.cache.provider.EntryType

/**
 * @author Jordan Abraham
 */
data class SpriteEntryType(
    override val id: Int,
    var sprites: List<Sprite> = emptyList()
) : EntryType(id)

data class Sprite(
    val id: Int,
    val width: Int,
    val height: Int,
    val pixels: IntArray
) {
    fun renderable() = width != 0 && height != 0 && pixels.isNotEmpty()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Sprite

        if (id != other.id) return false
        if (width != other.width) return false
        if (height != other.height) return false
        if (!pixels.contentEquals(other.pixels)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + width
        result = 31 * result + height
        result = 31 * result + pixels.contentHashCode()
        return result
    }
}
