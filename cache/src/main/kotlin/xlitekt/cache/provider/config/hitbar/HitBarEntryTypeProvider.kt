package xlitekt.cache.provider.config.hitbar

import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readUByte
import io.ktor.utils.io.core.readUShort
import xlitekt.cache.provider.EntryTypeProvider
import xlitekt.shared.buffer.readUIntSmart

/**
 * @author Jordan Abraham
 */
class HitBarEntryTypeProvider : EntryTypeProvider<HitBarEntryType>() {

    override fun load(): Map<Int, HitBarEntryType> = store
        .index(CONFIG_INDEX)
        .group(HITBAR_CONFIG)
        .files()
        .map { ByteReadPacket(it.data).loadEntryType(HitBarEntryType(it.id)) }
        .associateBy(HitBarEntryType::id)

    override tailrec fun ByteReadPacket.loadEntryType(type: HitBarEntryType): HitBarEntryType {
        when (val opcode = readUByte().toInt()) {
            0 -> { assertEmptyAndRelease(); return type }
            1 -> discard(2) // Unused.
            2 -> type.int1 = readUByte().toInt()
            3 -> type.int2 = readUByte().toInt()
            4 -> type.int3 = 0
            5 -> type.int5 = readUShort().toInt()
            6 -> discard(1) // Unused.
            7 -> type.frontSpriteId = readUIntSmart()
            8 -> type.backgroundSpriteId = readUIntSmart()
            11 -> type.int3 = readUShort().toInt()
            14 -> type.scale = readUByte().toInt()
            15 -> type.padding = readUByte().toInt()
            else -> throw IllegalArgumentException("Missing opcode $opcode.")
        }
        return loadEntryType(type)
    }
}
