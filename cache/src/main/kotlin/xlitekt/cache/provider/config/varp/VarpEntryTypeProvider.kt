package xlitekt.cache.provider.config.varp

import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readUByte
import io.ktor.utils.io.core.readUShort
import xlitekt.cache.provider.EntryTypeProvider

/**
 * @author Jordan Abraham
 */
class VarpEntryTypeProvider : EntryTypeProvider<VarpEntryType>() {

    override fun load(): Map<Int, VarpEntryType> = store
        .index(CONFIG_INDEX)
        .group(VARP_CONFIG)
        .files()
        .map { ByteReadPacket(it.data).loadEntryType(VarpEntryType(it.id)) }
        .associateBy(VarpEntryType::id)

    override tailrec fun ByteReadPacket.loadEntryType(type: VarpEntryType): VarpEntryType {
        when (val opcode = readUByte().toInt()) {
            0 -> { assertEmptyAndRelease(); return type }
            5 -> type.type = readUShort().toInt()
            else -> throw IllegalArgumentException("Missing opcode $opcode.")
        }
        return loadEntryType(type)
    }
}
