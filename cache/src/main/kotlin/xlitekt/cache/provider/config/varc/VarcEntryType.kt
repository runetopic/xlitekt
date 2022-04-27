package xlitekt.cache.provider.config.varc

import kotlinx.serialization.Serializable
import xlitekt.cache.provider.EntryType

/**
 * @author Jordan Abraham
 */
@Serializable
data class VarcEntryType(
    override val id: Int,
    var persist: Boolean = false
) : EntryType(id)
