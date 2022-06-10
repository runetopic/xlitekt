package xlitekt.cache.provider.config.hitsplat

import xlitekt.cache.provider.EntryTypeProvider
import xlitekt.shared.buffer.readShort
import xlitekt.shared.buffer.readStringCp1252NullCircumfixed
import xlitekt.shared.buffer.readUByte
import xlitekt.shared.buffer.readUIntSmart
import xlitekt.shared.buffer.readUMedium
import xlitekt.shared.buffer.readUShort
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
class HitSplatEntryTypeProvider : EntryTypeProvider<HitSplatEntryType>() {

    override fun load(): Map<Int, HitSplatEntryType> = store
        .index(CONFIG_INDEX)
        .group(HITSPLAT_CONFIG)
        .files()
        .map { ByteBuffer.wrap(it.data).loadEntryType(HitSplatEntryType(it.id)) }
        .associateBy(HitSplatEntryType::id)

    override tailrec fun ByteBuffer.loadEntryType(type: HitSplatEntryType): HitSplatEntryType {
        when (val opcode = readUByte()) {
            0 -> { assertEmptyAndRelease(); return type }
            1 -> type.fontId = readUIntSmart()
            2 -> type.textColor = readUMedium()
            3 -> type.leftSpriteId = readUIntSmart()
            4 -> type.spriteId2 = readUIntSmart()
            5 -> type.backgroundSprite = readUIntSmart()
            6 -> type.rightSpriteId = readUIntSmart()
            7 -> type.scrollToOffsetX = readShort()
            8 -> type.stringFormat = readStringCp1252NullCircumfixed()
            9 -> type.displayCycles = readUShort()
            10 -> type.scrollToOffsetY = readShort()
            11 -> type.fadeStartCycle = 0
            12 -> type.useDamage = readUByte()
            13 -> type.textOffsetY = readShort()
            14 -> type.fadeStartCycle = readUShort()
            17, 18 -> {
                type.transformVarbit = readUShort().let { if (it == 0xffff) -1 else it }
                type.transformVarp = readUShort().let { if (it == 0xffff) -1 else it }
                val prime = if (opcode == 17) -1 else readUShort().let { if (it == 0xffff) -1 else it }
                type.transforms = buildList {
                    repeat(readUByte() + 1) {
                        add(readUShort().let { if (it == 0xffff) -1 else it })
                    }
                    add(prime)
                }
            }
            else -> throw IllegalArgumentException("Missing opcode $opcode.")
        }
        return loadEntryType(type)
    }
}
