package xlitekt.cache.provider.config.kit

import kotlinx.serialization.Serializable
import xlitekt.cache.provider.EntryType

/**
 * @author Jordan Abraham
 */
@Serializable
data class KitEntryType(
    override val id: Int,
    var bodyPartId: Int = -1,
    var models: List<Int> = listOf(-1, -1, -1, -1, -1),
    var nonSelectable: Boolean = false
) : EntryType(id)
