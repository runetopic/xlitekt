package xlitekt.cache.provider.dbindex

import xlitekt.cache.db.DBValue
import xlitekt.cache.db.DBValueIndexMap
import xlitekt.cache.provider.EntryTypeProvider
import xlitekt.shared.buffer.method7754
import xlitekt.shared.buffer.readUByte
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
class DBIndexEntryTypeProvider : EntryTypeProvider<DBIndexEntryType>() {

    override fun load(): Map<Int, DBIndexEntryType> = store
        .index(DBTABLES_INDEX)
        .groups()
        .flatMap { group ->
            group.files().map { ByteBuffer.wrap(it.data).loadEntryType(DBIndexEntryType((group.id shl 8 and 0xff) or (it.id and 0xff))) }
        }
        .associateBy(DBIndexEntryType::id)

    override fun ByteBuffer.loadEntryType(type: DBIndexEntryType): DBIndexEntryType {
        val size = method7754()
        val field4670: MutableList<DBValue<*>?> = MutableList(size) { null }
        val field4673: MutableList<Map<Any, List<Int>>?> = MutableList(size) { null }

        repeat(size) {
            field4670[it] = DBValueIndexMap[readUByte()]
            val var5 = HashMap<Any, List<Int>>()
            repeat(method7754()) { _ ->
                val var6 = field4670[it]!!.read(this)!!
                var5[var6] = List(method7754()) { method7754() }
            }
            field4673[it] = var5
        }
        type.field4672 = field4670
        type.field4673 = field4673
        assertEmptyAndRelease()
        return type
    }
}
