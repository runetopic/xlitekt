package xlitekt.cache.provider.config.inv

import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readUByte
import io.ktor.utils.io.core.readUShort
import xlitekt.cache.provider.EntryTypeProvider

/**
 * @author Jordan Abraham
 */
class InvEntryTypeProvider : EntryTypeProvider<InvEntryType>() {

    override fun load(): Map<Int, InvEntryType> = store
        .index(CONFIG_INDEX)
        .group(INV_CONFIG)
        .files()
        .map { ByteReadPacket(it.data).loadEntryType(InvEntryType(it.id)) }
        .associateBy(InvEntryType::id)

    override fun ByteReadPacket.loadEntryType(type: InvEntryType): InvEntryType {
        when (val opcode = readUByte().toInt()) {
            0 -> { assertEmptyAndRelease(); return type }
            2 -> type.size = readUShort().toInt()
            else -> throw IllegalArgumentException("Missing opcode $opcode.")
        }
        return loadEntryType(type)
    }
}
