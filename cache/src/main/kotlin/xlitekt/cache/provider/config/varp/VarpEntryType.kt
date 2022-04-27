package xlitekt.cache.provider.config.varp

import kotlinx.serialization.Serializable
import xlitekt.cache.provider.EntryType

/**
 * @author Jordan Abraham
 */
@Serializable
data class VarpEntryType(
    override val id: Int,
    var type: Int = 0
) : EntryType(id)
