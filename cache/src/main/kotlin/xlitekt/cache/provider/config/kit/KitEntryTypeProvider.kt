package xlitekt.cache.provider.config.kit

import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readUByte
import io.ktor.utils.io.core.readUShort
import xlitekt.cache.provider.EntryTypeProvider
import java.lang.IllegalArgumentException

/**
 * @author Jordan Abraham
 */
class KitEntryTypeProvider : EntryTypeProvider<KitEntryType>() {

    override fun load(): Map<Int, KitEntryType> = store
        .index(CONFIG_INDEX)
        .group(KIT_CONFIG)
        .files()
        .map { ByteReadPacket(it.data).loadEntryType(KitEntryType(it.id)) }
        .associateBy(KitEntryType::id)

    override fun ByteReadPacket.loadEntryType(type: KitEntryType): KitEntryType {
        when (val opcode = readUByte().toInt()) {
            0 -> { assertEmptyAndRelease(); return type }
            1 -> type.bodyPartId = readUByte().toInt()
            2 -> repeat(readUByte().toInt()) {
                discard(2) // Discard models2.
            }
            3 -> type.nonSelectable = true
            40 -> repeat(readUByte().toInt()) {
                discard(4) // Discard recolor.
            }
            41 -> repeat(readUByte().toInt()) {
                discard(4) // Discard retexture.
            }
            in 60..69 -> type.models = type.models.toMutableList().apply {
                this[opcode - 60] = readUShort().toInt()
            }
            else -> throw IllegalArgumentException("Missing opcode $opcode.")
        }
        return loadEntryType(type)
    }
}
