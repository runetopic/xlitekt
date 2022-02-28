package xlitekt.cache.provider.config.struct

import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readUByte
import java.lang.IllegalArgumentException
import xlitekt.cache.provider.EntryTypeProvider

/**
 * @author Jordan Abraham
 */
class StructEntryTypeProvider : EntryTypeProvider<StructEntryType>() {

    override fun load(): Map<Int, StructEntryType> = store
        .index(CONFIG_INDEX)
        .group(STRUCT_CONFIG)
        .files()
        .map { ByteReadPacket(it.data).loadEntryType(StructEntryType(it.id)) }
        .associateBy(StructEntryType::id)

    override tailrec fun ByteReadPacket.loadEntryType(type: StructEntryType): StructEntryType {
        when (val opcode = readUByte().toInt()) {
            0 -> { assertEmptyAndRelease(); return type }
            249 -> type.params = readStringIntParameters()
            else -> throw IllegalArgumentException("Missing opcode $opcode.")
        }
        return loadEntryType(type)
    }
}