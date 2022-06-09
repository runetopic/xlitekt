package xlitekt.cache.provider.config.inv

import xlitekt.cache.provider.EntryTypeProvider
import xlitekt.shared.buffer.readUByte
import xlitekt.shared.buffer.readUShort
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
class InvEntryTypeProvider : EntryTypeProvider<InvEntryType>() {

    override fun load(): Map<Int, InvEntryType> = store
        .index(CONFIG_INDEX)
        .group(INV_CONFIG)
        .files()
        .map { ByteBuffer.wrap(it.data).loadEntryType(InvEntryType(it.id)) }
        .associateBy(InvEntryType::id)

    override fun ByteBuffer.loadEntryType(type: InvEntryType): InvEntryType {
        when (val opcode = readUByte()) {
            0 -> { assertEmptyAndRelease(); return type }
            2 -> type.size = readUShort()
            else -> throw IllegalArgumentException("Missing opcode $opcode.")
        }
        return loadEntryType(type)
    }
}
