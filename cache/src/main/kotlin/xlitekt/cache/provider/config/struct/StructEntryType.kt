package xlitekt.cache.provider.config.struct

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import xlitekt.cache.provider.EntryType

/**
 * @author Jordan Abraham
 */
@Serializable
data class StructEntryType(
    override val id: Int,
    var params: Map<Int, @Contextual Any> = mapOf()
) : EntryType(id)
