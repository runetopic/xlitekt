package xlitekt.cache.provider.config.varp

import xlitekt.cache.provider.EntryTypeProvider
import xlitekt.shared.buffer.readUByte
import xlitekt.shared.buffer.readUShort
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
class VarpEntryTypeProvider : EntryTypeProvider<VarpEntryType>() {

    override fun load(): Map<Int, VarpEntryType> = store
        .index(CONFIG_INDEX)
        .group(VARP_CONFIG)
        .files()
        .map { ByteBuffer.wrap(it.data).loadEntryType(VarpEntryType(it.id)) }
        .associateBy(VarpEntryType::id)

    override tailrec fun ByteBuffer.loadEntryType(type: VarpEntryType): VarpEntryType {
        when (val opcode = readUByte()) {
            0 -> { assertEmptyAndRelease(); return type }
            5 -> type.type = readUShort()
            else -> throw IllegalArgumentException("Missing opcode $opcode.")
        }
        return loadEntryType(type)
    }
}
