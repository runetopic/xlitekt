package xlitekt.cache.provider.config.struct

import xlitekt.cache.provider.EntryTypeProvider
import xlitekt.shared.buffer.readUByte
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
class StructEntryTypeProvider : EntryTypeProvider<StructEntryType>() {

    override fun load(): Map<Int, StructEntryType> = store
        .index(CONFIG_INDEX)
        .group(STRUCT_CONFIG)
        .files()
        .map { ByteBuffer.wrap(it.data).loadEntryType(StructEntryType(it.id)) }
        .associateBy(StructEntryType::id)

    override tailrec fun ByteBuffer.loadEntryType(type: StructEntryType): StructEntryType {
        when (val opcode = readUByte()) {
            0 -> { assertEmptyAndRelease(); return type }
            249 -> type.params = readStringIntParameters()
            else -> throw IllegalArgumentException("Missing opcode $opcode.")
        }
        return loadEntryType(type)
    }
}
