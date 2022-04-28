package xlitekt.cache.provider.config.worldmap

import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readInt
import io.ktor.utils.io.core.readShort
import io.ktor.utils.io.core.readUByte
import io.ktor.utils.io.core.readUShort
import xlitekt.cache.provider.EntryTypeProvider
import xlitekt.shared.buffer.readStringCp1252NullTerminated
import xlitekt.shared.buffer.readUIntSmart
import xlitekt.shared.buffer.readUMedium

/**
 * @author Jordan Abraham
 */
class WorldMapElementEntryTypeProvider : EntryTypeProvider<WorldMapElementEntryType>() {

    override fun load(): Map<Int, WorldMapElementEntryType> = store
        .index(CONFIG_INDEX)
        .group(WORLD_MAP_CONFIG)
        .files()
        .map { ByteReadPacket(it.data).loadEntryType(WorldMapElementEntryType(it.id)) }
        .associateBy(WorldMapElementEntryType::id)

    override tailrec fun ByteReadPacket.loadEntryType(type: WorldMapElementEntryType): WorldMapElementEntryType {
        when (val opcode = readUByte().toInt()) {
            0 -> { assertEmptyAndRelease(); return type }
            1 -> type.sprite1 = readUIntSmart()
            2 -> type.sprite2 = readUIntSmart()
            3 -> type.name = readStringCp1252NullTerminated()
            4 -> type.location = readUMedium()
            5 -> discard(3) // Unused
            6 -> type.textSize = readUByte().toInt()
            7 -> {
                val mask = readUByte().toInt()
                if (mask and 1 == 0) {
                    type.field1758 = false
                }
                if (mask and 2 == 2) {
                    type.field1759 = true
                }
            }
            8 -> discard(1) // Unused
            in 10..14 -> type.menuActions = type.menuActions.toMutableList().apply {
                this[opcode - 10] = readStringCp1252NullTerminated()
            }
            15 -> {
                val field1762Size = readUByte().toInt()
                type.field1762 = IntArray(field1762Size * 2)

                repeat(field1762Size * 2) {
                    type.field1762!![it] = readShort().toInt()
                }

                discard(4) // Unused

                val field1749Size = readUByte().toInt()
                type.field1749 = IntArray(field1749Size)

                repeat(field1749Size) {
                    type.field1749!![it] = readInt()
                }

                type.field1769 = ByteArray(field1762Size)
                repeat(field1762Size) {
                    type.field1769!![it] = readByte()
                }
            }
            17 -> type.menuTargetName = readStringCp1252NullTerminated()
            18 -> readUIntSmart() // Unused
            19 -> type.category = readUShort().toInt()
            21 -> discard(4) // Unused
            22 -> discard(4) // Unused
            23 -> discard(3) // Unused
            24 -> discard(4) // Unused
            25 -> readUIntSmart() // Unused
            28 -> discard(1) // Unused
            29 -> type.horizontalAlignment = readUByte().toInt()
            30 -> type.verticalAlignment = readUByte().toInt()
            else -> throw IllegalArgumentException("Missing opcode $opcode.")
        }
        return loadEntryType(type)
    }
}
