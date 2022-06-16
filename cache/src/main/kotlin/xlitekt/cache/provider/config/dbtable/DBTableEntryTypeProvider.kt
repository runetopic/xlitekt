package xlitekt.cache.provider.config.dbtable

import xlitekt.cache.db.DBValueIndexMap
import xlitekt.cache.provider.EntryTypeProvider
import xlitekt.shared.buffer.readUByte
import xlitekt.shared.buffer.readUShortSmart
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
class DBTableEntryTypeProvider : EntryTypeProvider<DBTableEntryType>() {

    override fun load(): Map<Int, DBTableEntryType> = store
        .index(CONFIG_INDEX)
        .group(DB_TABLE_CONFIG)
        .files()
        .map { ByteBuffer.wrap(it.data).loadEntryType(DBTableEntryType(it.id)) }
        .associateBy(DBTableEntryType::id)

    override tailrec fun ByteBuffer.loadEntryType(type: DBTableEntryType): DBTableEntryType {
        when (val opcode = readUByte()) {
            0 -> { assertEmptyAndRelease(); return type }
            1 -> {
                val size = readUByte()
                if (type.field4668 == null) {
                    type.field4668 = arrayOfNulls(size)
                }
                var setting = readUByte()
                while (setting != 255) {
                    val column = setting and 127
                    val isDefault = setting and 128 != 0
                    val var7 = IntArray(readUByte()) { readUShortSmart() }
                    type.field4668!![column] = var7
                    if (isDefault) { // L: 54
                        if (type.field4669 == null) {
                            type.field4669 = arrayOfNulls(type.field4668!!.size)
                        }
                        type.field4669!![column] = method4353(var7)
                    }
                    setting = readUByte()
                }
            }
            else -> throw IllegalArgumentException("Missing opcode $opcode.")
        }
        return loadEntryType(type)
    }

    private fun ByteBuffer.method4353(var1: IntArray): Array<Any?> {
        val var2 = readUShortSmart()
        val var3 = arrayOfNulls<Any>(var1.size * var2)

        repeat(var2) { var4 ->
            repeat(var1.size) { var5 ->
                val var6 = var1.size * var4 + var5
                var3[var6] = DBValueIndexMap[var1[var5]]!!.read(this)!!
            }
        }
        return var3
    }
}
