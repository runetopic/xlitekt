package xlitekt.cache.provider.config.varc

import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readUByte
import xlitekt.cache.provider.EntryTypeProvider

/**
 * @author Jordan Abraham
 */
class VarcEntryTypeProvider : EntryTypeProvider<VarcEntryType>() {

    override fun load(): Map<Int, VarcEntryType> = store
        .index(CONFIG_INDEX)
        .group(VARC_CONFIG)
        .files()
        .map { ByteReadPacket(it.data).loadEntryType(VarcEntryType(it.id)) }
        .associateBy(VarcEntryType::id)

    override tailrec fun ByteReadPacket.loadEntryType(type: VarcEntryType): VarcEntryType {
        when (val opcode = readUByte().toInt()) {
            0 -> { assertEmptyAndRelease(); return type }
            2 -> type.persist = true
            else -> throw IllegalArgumentException("Missing opcode $opcode.")
        }
        return loadEntryType(type)
    }
}
