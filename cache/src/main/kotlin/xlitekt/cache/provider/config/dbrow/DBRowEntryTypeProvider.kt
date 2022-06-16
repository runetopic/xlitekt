package xlitekt.cache.provider.config.dbrow

import xlitekt.cache.db.DBValueIndexMap
import xlitekt.cache.provider.EntryTypeProvider
import xlitekt.shared.buffer.method7754
import xlitekt.shared.buffer.readUByte
import xlitekt.shared.buffer.readUShortSmart
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
class DBRowEntryTypeProvider : EntryTypeProvider<DBRowEntryType>() {

    override fun load(): Map<Int, DBRowEntryType> = store
        .index(CONFIG_INDEX)
        .group(DB_ROW_CONFIG)
        .files()
        .map { ByteBuffer.wrap(it.data).loadEntryType(DBRowEntryType(it.id)) }
        .associateBy(DBRowEntryType::id)

    override tailrec fun ByteBuffer.loadEntryType(type: DBRowEntryType): DBRowEntryType {
        when (val opcode = readUByte()) {
            0 -> { assertEmptyAndRelease(); return type }
            3 -> {
                val size = readUByte()
                if (type.field4676 == null) {
                    type.field4676 = arrayOfNulls(size)
                    type.field4677 = arrayOfNulls(size)
                }
                var var4 = readUByte()
                while (var4 != 255) {
                    val var6 = IntArray(readUByte()) { readUShortSmart() }
                    val var16 = type.field4676!!
                    val var10 = readUShortSmart()
                    val var11 = arrayOfNulls<Any?>(var6.size * var10)
                    repeat(var10) { var12 ->
                        for (var13 in var6.indices) {
                            val var14 = var13 + var6.size * var12
                            var11[var14] = DBValueIndexMap[var6[var13]]!!.read(this)!!
                        }
                    }
                    var16[var4] = var11
                    type.field4677!![var4] = var6
                    var4 = readUByte()
                }
            }
            4 -> type.field4678 = method7754()
            else -> throw IllegalArgumentException("Missing opcode $opcode.")
        }
        return loadEntryType(type)
    }
}
