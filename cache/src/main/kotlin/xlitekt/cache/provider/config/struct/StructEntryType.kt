package xlitekt.cache.provider.config.struct

import xlitekt.cache.provider.EntryType

/**
 * @author Jordan Abraham
 */
data class StructEntryType(
    override val id: Int,
    var params: Map<Int, Any> = mapOf()
) : EntryType(id)
