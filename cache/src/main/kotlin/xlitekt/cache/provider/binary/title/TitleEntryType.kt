package xlitekt.cache.provider.binary.title

import xlitekt.cache.provider.EntryType

/**
 * @author Jordan Abraham
 */
data class TitleEntryType(
    override val id: Int,
    var pixels: ByteArray? = null
) : EntryType(id)
