package xlitekt.cache.provider.config.npc

import xlitekt.cache.provider.EntryTypeProvider
import xlitekt.shared.buffer.discard
import xlitekt.shared.buffer.readByte
import xlitekt.shared.buffer.readStringCp1252NullTerminated
import xlitekt.shared.buffer.readUByte
import xlitekt.shared.buffer.readUShort
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
class NPCEntryTypeProvider : EntryTypeProvider<NPCEntryType>() {

    override fun load(): Map<Int, NPCEntryType> = store
        .index(CONFIG_INDEX)
        .group(NPC_CONFIG)
        .files()
        .map { ByteBuffer.wrap(it.data).loadEntryType(NPCEntryType(it.id)) }
        .associateBy(NPCEntryType::id)

    override tailrec fun ByteBuffer.loadEntryType(type: NPCEntryType): NPCEntryType {
        when (val opcode = readUByte()) {
            0 -> { assertEmptyAndRelease(); return type }
            1 -> type.models = buildList {
                repeat(readUByte()) {
                    add(readUShort())
                }
            }
            2 -> type.name = readStringCp1252NullTerminated()
            12 -> type.size = readUByte()
            13 -> type.idleSequence = readUShort()
            14 -> type.walkSequence = readUShort()
            15 -> type.turnLeftSequence = readUShort()
            16 -> type.turnRightSequence = readUShort()
            17 -> {
                type.walkSequence = readUShort()
                type.walkBackSequence = readUShort()
                type.walkLeftSequence = readUShort()
                type.walkRightSequence = readUShort()
            }
            18 -> discard(2) // Unused.
            in 30..34 -> type.actions = type.actions.toMutableList().apply {
                this[opcode - 30] = readStringCp1252NullTerminated().let { if (it.equals("Hidden", true)) "null" else it }
            }
            40 -> repeat(readUByte()) {
                discard(4) // Discard recolor.
            }
            41 -> repeat(readUByte()) {
                discard(4) // Discard retexture.
            }
            60 -> repeat(readUByte()) {
                discard(2) // Discard unknown field.
            }
            93 -> type.drawMapDot = false
            95 -> type.combatLevel = readUShort()
            97 -> type.widthScale = readUShort()
            98 -> type.heightScale = readUShort()
            99 -> type.isVisible = true
            100 -> type.ambient = readByte()
            101 -> type.contrast = readByte() * 5
            102 -> type.headIconPrayer = readUShort()
            103 -> type.rotation = readUShort()
            106, 118 -> {
                type.transformVarbit = readUShort().let { if (it == 0xffff) -1 else it }
                type.transformVarp = readUShort().let { if (it == 0xffff) -1 else it }
                val prime = if (opcode == 106) -1 else readUShort().let { if (it == 0xffff) -1 else it }
                type.transforms = buildList {
                    repeat(readUByte() + 1) {
                        add(readUShort().let { if (it == 0xffff) -1 else it })
                    }
                    add(prime)
                }
            }
            107 -> type.isInteractable = false
            109 -> type.isClickable = false
            111 -> type.isFollower = true
            114 -> type.runSequence = readUShort()
            115 -> {
                type.runSequence = readUShort()
                type.runBackSequence = readUShort()
                type.runLeftSequence = readUShort()
                type.runRightSequence = readUShort()
            }
            116 -> type.crawlSequence = readUShort()
            117 -> {
                type.crawlSequence = readUShort()
                type.crawlBackSequence = readUShort()
                type.crawlLeftSequence = readUShort()
                type.crawlRightSequence = readUShort()
            }
            249 -> type.params = readStringIntParameters()
            else -> throw IllegalArgumentException("Missing opcode $opcode.")
        }
        return loadEntryType(type)
    }
}
