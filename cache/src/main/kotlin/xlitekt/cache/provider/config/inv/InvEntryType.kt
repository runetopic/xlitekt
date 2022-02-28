package xlitekt.cache.provider.config.inv

import xlitekt.cache.provider.EntryType

/**
 * @author Jordan Abraham
 */
data class InvEntryType(
    override val id: Int,
    var size: Int = 0
) : EntryType(id)
