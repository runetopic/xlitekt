package xlitekt.cache.provider.config.hitsplat

import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readShort
import io.ktor.utils.io.core.readUByte
import io.ktor.utils.io.core.readUShort
import xlitekt.cache.provider.EntryTypeProvider
import xlitekt.shared.buffer.readStringCp1252NullCircumfixed
import xlitekt.shared.buffer.readUIntSmart
import xlitekt.shared.buffer.readUMedium
import java.lang.IllegalArgumentException

/**
 * @author Jordan Abraham
 */
class HitSplatEntryTypeProvider : EntryTypeProvider<HitSplatEntryType>() {

    override fun load(): Map<Int, HitSplatEntryType> = store
        .index(CONFIG_INDEX)
        .group(HITSPLAT_CONFIG)
        .files()
        .map { ByteReadPacket(it.data).loadEntryType(HitSplatEntryType(it.id)) }
        .associateBy(HitSplatEntryType::id)

    override tailrec fun ByteReadPacket.loadEntryType(type: HitSplatEntryType): HitSplatEntryType {
        when (val opcode = readUByte().toInt()) {
            0 -> { assertEmptyAndRelease(); return type }
            1 -> type.fontId = readUIntSmart()
            2 -> type.textColor = readUMedium()
            3 -> type.leftSpriteId = readUIntSmart()
            4 -> type.spriteId2 = readUIntSmart()
            5 -> type.backgroundSprite = readUIntSmart()
            6 -> type.rightSpriteId = readUIntSmart()
            7 -> type.scrollToOffsetX = readShort().toInt()
            8 -> type.stringFormat = readStringCp1252NullCircumfixed()
            9 -> type.displayCycles = readUShort().toInt()
            10 -> type.scrollToOffsetY = readShort().toInt()
            11 -> type.fadeStartCycle = 0
            12 -> type.useDamage = readUByte().toInt()
            13 -> type.textOffsetY = readShort().toInt()
            14 -> type.fadeStartCycle = readUShort().toInt()
            17, 18 -> {
                type.transformVarbit = readUShort().toInt().let { if (it == 0xffff) -1 else it }
                type.transformVarp = readUShort().toInt().let { if (it == 0xffff) -1 else it }
                val prime = if (opcode == 17) -1 else readUShort().toInt().let { if (it == 0xffff) -1 else it }
                type.transforms = buildList {
                    repeat(readUByte().toInt() + 1) {
                        add(readUShort().toInt().let { if (it == 0xffff) -1 else it })
                    }
                    add(prime)
                }
            }
            else -> throw IllegalArgumentException("Missing opcode $opcode.")
        }
        return loadEntryType(type)
    }
}
