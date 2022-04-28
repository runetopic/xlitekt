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
    var horizontalAlignment: Int = 0,
    var verticalAlignment: Int = 0,
    var category: Int = -1,
    var field1762: IntArray? = null,
    var field1749: IntArray? = null,
    var field1769: ByteArray? = null,
    var menuTargetName: String = ""
) : EntryType(id) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WorldMapElementEntryType

        if (id != other.id) return false
        if (sprite1 != other.sprite1) return false
        if (sprite2 != other.sprite2) return false
        if (name != other.name) return false
        if (location != other.location) return false
        if (textSize != other.textSize) return false
        if (field1758 != other.field1758) return false
        if (field1759 != other.field1759) return false
        if (menuActions != other.menuActions) return false
        if (field1770 != other.field1770) return false
        if (field1764 != other.field1764) return false
        if (field1763 != other.field1763) return false
        if (field1766 != other.field1766) return false
        if (horizontalAlignment != other.horizontalAlignment) return false
        if (verticalAlignment != other.verticalAlignment) return false
        if (category != other.category) return false
        if (field1762 != null) {
            if (other.field1762 == null) return false
            if (!field1762.contentEquals(other.field1762)) return false
        } else if (other.field1762 != null) return false
        if (field1749 != null) {
            if (other.field1749 == null) return false
            if (!field1749.contentEquals(other.field1749)) return false
        } else if (other.field1749 != null) return false
        if (field1769 != null) {
            if (other.field1769 == null) return false
            if (!field1769.contentEquals(other.field1769)) return false
        } else if (other.field1769 != null) return false
        if (menuTargetName != other.menuTargetName) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + sprite1
        result = 31 * result + sprite2
        result = 31 * result + name.hashCode()
        result = 31 * result + location
        result = 31 * result + textSize
        result = 31 * result + field1758.hashCode()
        result = 31 * result + field1759.hashCode()
        result = 31 * result + menuActions.hashCode()
        result = 31 * result + field1770
        result = 31 * result + field1764
        result = 31 * result + field1763
        result = 31 * result + field1766
        result = 31 * result + horizontalAlignment
        result = 31 * result + verticalAlignment
        result = 31 * result + category
        result = 31 * result + (field1762?.contentHashCode() ?: 0)
        result = 31 * result + (field1749?.contentHashCode() ?: 0)
        result = 31 * result + (field1769?.contentHashCode() ?: 0)
        result = 31 * result + menuTargetName.hashCode()
        return result
    }
}
