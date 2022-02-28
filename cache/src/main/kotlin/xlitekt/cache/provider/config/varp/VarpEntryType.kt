package xlitekt.cache.provider.config.varp

import xlitekt.cache.provider.EntryType

/**
 * @author Jordan Abraham
 */
data class VarpEntryType(
    override val id: Int,
    var type: Int = 0
) : EntryType(id)
