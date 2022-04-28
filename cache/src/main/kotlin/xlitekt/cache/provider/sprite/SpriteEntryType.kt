package xlitekt.cache.provider.sprite

import xlitekt.cache.provider.EntryType

/**
 * @author Jordan Abraham
 */
data class SpriteEntryType(
    override val id: Int,
    var offsetsX: IntArray? = null,
    var offsetsY: IntArray? = null,
    var widths: IntArray? = null,
    var heights: IntArray? = null,
    var sprites: List<Sprite> = emptyList()
) : EntryType(id) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SpriteEntryType

        if (id != other.id) return false
        if (offsetsX != null) {
            if (other.offsetsX == null) return false
            if (!offsetsX.contentEquals(other.offsetsX)) return false
        } else if (other.offsetsX != null) return false
        if (offsetsY != null) {
            if (other.offsetsY == null) return false
            if (!offsetsY.contentEquals(other.offsetsY)) return false
        } else if (other.offsetsY != null) return false
        if (widths != null) {
            if (other.widths == null) return false
            if (!widths.contentEquals(other.widths)) return false
        } else if (other.widths != null) return false
        if (heights != null) {
            if (other.heights == null) return false
            if (!heights.contentEquals(other.heights)) return false
        } else if (other.heights != null) return false
        if (sprites != other.sprites) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + (offsetsX?.contentHashCode() ?: 0)
        result = 31 * result + (offsetsY?.contentHashCode() ?: 0)
        result = 31 * result + (widths?.contentHashCode() ?: 0)
        result = 31 * result + (heights?.contentHashCode() ?: 0)
        result = 31 * result + sprites.hashCode()
        return result
    }
}

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
