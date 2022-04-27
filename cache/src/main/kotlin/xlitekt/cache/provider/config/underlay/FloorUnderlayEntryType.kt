package xlitekt.cache.provider.config.underlay

import kotlinx.serialization.Serializable
import xlitekt.cache.provider.EntryType

/**
 * @author Jordan Abraham
 */
@Serializable
data class FloorUnderlayEntryType(
    override val id: Int,
    var rgb: Int = 0
) : EntryType(id)
