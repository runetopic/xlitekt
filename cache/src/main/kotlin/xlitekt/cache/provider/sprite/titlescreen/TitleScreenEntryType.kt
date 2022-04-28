package xlitekt.cache.provider.sprite.titlescreen

import xlitekt.cache.provider.EntryType

/**
 * @author Jordan Abraham
 */
data class TitleScreenEntryType(
    override val id: Int,
    var name: String? = null
) : EntryType(id)
