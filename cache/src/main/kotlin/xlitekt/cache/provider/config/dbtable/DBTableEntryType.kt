package xlitekt.cache.provider.config.dbtable

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import xlitekt.cache.provider.EntryType

/**
 * @author Jordan Abraham
 */
@Serializable
data class DBTableEntryType(
    override val id: Int,
    var field4668: Array<IntArray?>? = null,
    var field4669: Array<Array<@Contextual Any?>?>? = null
) : EntryType(id)
