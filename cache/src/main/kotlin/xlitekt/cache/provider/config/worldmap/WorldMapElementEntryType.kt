package xlitekt.cache.provider.config.worldmap

import kotlinx.serialization.Serializable
import xlitekt.cache.provider.EntryType

/**
 * @author Jordan Abraham
 */
@Serializable
data class WorldMapElementEntryType(
    override val id: Int,
    var sprite1: Int = -1,
    var sprite2: Int = -1,
    var name: String = "",
    var location: Int = 0,
    var textSize: Int = 0,
    var field1758: Boolean = true,
    var field1759: Boolean = false,
    var menuActions: List<String> = List(5) { "null" },
    var field1770: Int = Int.MAX_VALUE,
    var field1764: Int = Int.MAX_VALUE,
    var field1763: Int = Int.MIN_VALUE,
    var field1766: Int = Int.MIN_VALUE,
//    var horizontalAlignment = HorizontalAlignment.HorizontalAlignment_centered,
//    var verticalAlignment = VerticalAlignment.VerticalAlignment_centered,
    var horizontalAlignment: Int = 0,
    var verticalAlignment: Int = 0,
    var category: Int = -1,
    var field1762: IntArray? = null,
    var field1749: IntArray? = null,
    var field1769: ByteArray? = null,
    var menuTargetName: String = ""
) : EntryType(id)
