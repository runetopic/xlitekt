package com.runetopic.xlitekt.cache.provider.config.npc

import com.runetopic.xlitekt.cache.provider.EntryTypeProvider
import com.runetopic.xlitekt.cache.provider.EntryTypeProvider.Companion.CONFIG_INDEX
import com.runetopic.xlitekt.cache.provider.EntryTypeProvider.Companion.NPC_CONFIG
import com.runetopic.xlitekt.util.ext.readStringCp1252NullTerminated
import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readUByte
import io.ktor.utils.io.core.readUShort
import java.lang.IllegalArgumentException

/**
 * @author Jordan Abraham
 */
class NPCEntryTypeProvider : EntryTypeProvider<Int, NPCEntryType>() {

    override fun load(): Map<Int, NPCEntryType> = store
        .index(CONFIG_INDEX)
        .group(NPC_CONFIG)
        .files()
        .map { ByteReadPacket(it.data).loadEntryType(NPCEntryType(it.id)) }
        .associateBy { it.id }

    override tailrec fun ByteReadPacket.loadEntryType(type: NPCEntryType): NPCEntryType {
        when (val opcode = readUByte().toInt()) {
            0 -> { assertEmptyAndRelease(); return type }
            1 -> type.models = buildList {
                repeat(readUByte().toInt()) {
                    add(readUShort().toInt())
                }
            }
            2 -> type.name = readStringCp1252NullTerminated()
            12 -> type.size = readUByte().toInt()
            13 -> type.idleSequence = readUShort().toInt()
            14 -> type.walkSequence = readUShort().toInt()
            15 -> type.turnLeftSequence = readUShort().toInt()
            16 -> type.turnRightSequence = readUShort().toInt()
            17 -> {
                type.walkSequence = readUShort().toInt()
                type.walkBackSequence = readUShort().toInt()
                type.walkLeftSequence = readUShort().toInt()
                type.walkRightSequence = readUShort().toInt()
            }
            18 -> discard(2) // Unused.
            in 30..34 -> type.actions = type.actions.toMutableList().apply {
                this[opcode - 30] = readStringCp1252NullTerminated().let { if (it.equals("Hidden", true)) "null" else it }
            }
            40 -> repeat(readUByte().toInt()) {
                discard(4) // Discard recolor.
            }
            41 -> repeat(readUByte().toInt()) {
                discard(4) // Discard retexture.
            }
            60 -> repeat(readUByte().toInt()) {
                discard(2) // Discard unknown field.
            }
            93 -> type.drawMapDot = false
            95 -> type.combatLevel = readUShort().toInt()
            97 -> type.widthScale = readUShort().toInt()
            98 -> type.heightScale = readUShort().toInt()
            99 -> type.isVisible = true
            100 -> type.ambient = readByte().toInt()
            101 -> type.contrast = readByte() * 5
            102 -> type.headIconPrayer = readUShort().toInt()
            103 -> type.rotation = readUShort().toInt()
            106, 118 -> {
                type.transformVarbit = readUShort().toInt().let { if (it == 0xffff) -1 else it }
                type.transformVarp = readUShort().toInt().let { if (it == 0xffff) -1 else it }
                val prime = if (opcode == 106) -1 else readUShort().toInt().let { if (it == 0xffff) -1 else it }
                type.transforms = buildList {
                    repeat(readUByte().toInt() + 1) {
                        add(readUShort().toInt().let { if (it == 0xffff) -1 else it })
                    }
                    add(prime)
                }
            }
            107 -> type.isInteractable = false
            109 -> type.isClickable = false
            111 -> type.isFollower = true
            249 -> type.params = readStringIntParameters()
            else -> throw IllegalArgumentException("Missing opcode $opcode.")
        }
        return loadEntryType(type)
    }
}
