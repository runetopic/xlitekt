package xlitekt.cache.provider.config.varc

import xlitekt.cache.provider.EntryTypeProvider
import xlitekt.shared.buffer.readUByte
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
class VarcEntryTypeProvider : EntryTypeProvider<VarcEntryType>() {

    override fun load(): Map<Int, VarcEntryType> = store
        .index(CONFIG_INDEX)
        .group(VARC_CONFIG)
        .files()
        .map { ByteBuffer.wrap(it.data).loadEntryType(VarcEntryType(it.id)) }
        .associateBy(VarcEntryType::id)

    override tailrec fun ByteBuffer.loadEntryType(type: VarcEntryType): VarcEntryType {
        when (val opcode = readUByte()) {
            0 -> { assertEmptyAndRelease(); return type }
            2 -> type.persist = true
            else -> throw IllegalArgumentException("Missing opcode $opcode.")
        }
        return loadEntryType(type)
    }
}
