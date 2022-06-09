package xlitekt.cache.provider.config.hitbar

import xlitekt.cache.provider.EntryTypeProvider
import xlitekt.shared.buffer.discard
import xlitekt.shared.buffer.readUByte
import xlitekt.shared.buffer.readUIntSmart
import xlitekt.shared.buffer.readUShort
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
class HitBarEntryTypeProvider : EntryTypeProvider<HitBarEntryType>() {

    override fun load(): Map<Int, HitBarEntryType> = store
        .index(CONFIG_INDEX)
        .group(HITBAR_CONFIG)
        .files()
        .map { ByteBuffer.wrap(it.data).loadEntryType(HitBarEntryType(it.id)) }
        .associateBy(HitBarEntryType::id)

    override tailrec fun ByteBuffer.loadEntryType(type: HitBarEntryType): HitBarEntryType {
        when (val opcode = readUByte()) {
            0 -> { assertEmptyAndRelease(); return type }
            1 -> discard(2) // Unused.
            2 -> type.int1 = readUByte()
            3 -> type.int2 = readUByte()
            4 -> type.int3 = 0
            5 -> type.int5 = readUShort()
            6 -> discard(1) // Unused.
            7 -> type.frontSpriteId = readUIntSmart()
            8 -> type.backgroundSpriteId = readUIntSmart()
            11 -> type.int3 = readUShort()
            14 -> type.scale = readUByte()
            15 -> type.padding = readUByte()
            else -> throw IllegalArgumentException("Missing opcode $opcode.")
        }
        return loadEntryType(type)
    }
}
