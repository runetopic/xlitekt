package xlitekt.cache.provider.font

import kotlinx.serialization.Serializable
import xlitekt.cache.provider.EntryType

/**
 * @author Jordan Abraham
 */
@Serializable
data class FontEntryType(
    override val id: Int,
    var name: String? = null,
    var ascent: Int = 0,
    var offsetsX: IntArray? = null,
    var offsetsY: IntArray? = null,
    var widths: IntArray? = null,
    var heights: IntArray? = null,
    var kerning: ByteArray? = null,
    var maxAscent: Int = 0,
    var maxDescent: Int = 0
) : EntryType(id)
