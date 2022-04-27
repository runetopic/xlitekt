package xlitekt.cache.provider.config.inv

import kotlinx.serialization.Serializable
import xlitekt.cache.provider.EntryType

/**
 * @author Jordan Abraham
 */
@Serializable
data class InvEntryType(
    override val id: Int,
    var size: Int = 0
) : EntryType(id)
