package xlitekt.cache.provider.config.overlay

import kotlinx.serialization.Serializable
import xlitekt.cache.provider.EntryType

/**
 * @author Jordan Abraham
 */
@Serializable
data class FloorOverlayEntryType(
    override val id: Int,
    var primaryRgb: Int = 0,
    var texture: Int = -1,
    var hideUnderlay: Boolean = true,
    var secondaryRgb: Int = -1
) : EntryType(id)
