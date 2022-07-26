package xlitekt.cache.provider.dbindex

import xlitekt.cache.db.DBValue
import xlitekt.cache.provider.EntryType

/**
 * @author Jordan Abraham
 */
data class DBIndexEntryType(
    override val id: Int,
    var field4672: List<DBValue<*>?>? = null,
    var field4673: List<Map<Any, List<Int>>?>? = null
) : EntryType(id)
