package xlitekt.cache.provider.config.kit

import xlitekt.cache.provider.EntryTypeProvider
import xlitekt.shared.buffer.discard
import xlitekt.shared.buffer.readUByte
import xlitekt.shared.buffer.readUShort
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
class KitEntryTypeProvider : EntryTypeProvider<KitEntryType>() {

    override fun load(): Map<Int, KitEntryType> = store
        .index(CONFIG_INDEX)
        .group(KIT_CONFIG)
        .files()
        .map { ByteBuffer.wrap(it.data).loadEntryType(KitEntryType(it.id)) }
        .associateBy(KitEntryType::id)

    override fun ByteBuffer.loadEntryType(type: KitEntryType): KitEntryType {
        when (val opcode = readUByte()) {
            0 -> { assertEmptyAndRelease(); return type }
            1 -> type.bodyPartId = readUByte()
            2 -> repeat(readUByte()) {
                discard(2) // Discard models2.
            }
            3 -> type.nonSelectable = true
            40 -> repeat(readUByte()) {
                discard(4) // Discard recolor.
            }
            41 -> repeat(readUByte()) {
                discard(4) // Discard retexture.
            }
            in 60..69 -> type.models = type.models.toMutableList().apply {
                this[opcode - 60] = readUShort()
            }
            else -> throw IllegalArgumentException("Missing opcode $opcode.")
        }
        return loadEntryType(type)
    }
}
