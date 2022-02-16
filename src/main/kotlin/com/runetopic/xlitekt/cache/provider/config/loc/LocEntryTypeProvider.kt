package com.runetopic.xlitekt.cache.provider.config.loc

import com.runetopic.xlitekt.cache.provider.EntryTypeProvider
import com.runetopic.xlitekt.shared.buffer.readStringCp1252NullTerminated
import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readShort
import io.ktor.utils.io.core.readUByte
import io.ktor.utils.io.core.readUShort
import java.lang.IllegalArgumentException

/**
 * @author Jordan Abraham
 */
class LocEntryTypeProvider : EntryTypeProvider<LocEntryType>() {

    override fun load(): Map<Int, LocEntryType> = store
        .index(CONFIG_INDEX)
        .group(LOC_CONFIG)
        .files()
        .map { ByteReadPacket(it.data).loadEntryType(LocEntryType(it.id)) }
        .associateBy(LocEntryType::id)

    override tailrec fun ByteReadPacket.loadEntryType(type: LocEntryType): LocEntryType {
        when (val opcode = readUByte().toInt()) {
            0 -> { assertEmptyAndRelease(); return type }
            1 -> repeat(readUByte().toInt()) {
                discard(3) // Discard models/modelIds.
            }
            2 -> type.name = readStringCp1252NullTerminated()
            5 -> repeat(readUByte().toInt()) {
                discard(2) // Discard models/modelIds.
            }
            14 -> type.sizeX = readUByte().toInt()
            15 -> type.sizeY = readUByte().toInt()
            17 -> {
                type.interactType = 0
                type.boolean1 = false
            }
            18 -> type.boolean1 = false
            19 -> type.int1 = readUByte().toInt()
            21 -> type.clipType = 0
            22 -> type.nonFlatShading = true
            23 -> type.modelClipped = true
            24 -> type.animationId = readUShort().toInt().let { if (it == 0xffff) -1 else it }
            27 -> type.interactType = 1
            28 -> type.int2 = readUByte().toInt()
            29 -> type.ambient = readByte().toInt()
            39 -> type.contrast = readByte() * 25
            in 30..34 -> type.actions = type.actions.toMutableList().apply {
                this[opcode - 30] = readStringCp1252NullTerminated().let { if (it.equals("Hidden", true)) "null" else it }
            }
            40 -> repeat(readUByte().toInt()) {
                discard(4) // Discard recolor.
            }
            41 -> repeat(readUByte().toInt()) {
                discard(4) // Discard retexture.
            }
            61 -> discard(2) // Unused.
            62 -> type.isRotated = true
            64 -> type.clipped = false
            65 -> type.modelSizeX = readUShort().toInt()
            66 -> type.modelHeight = readUShort().toInt()
            67 -> type.modelSizeY = readUShort().toInt()
            68 -> type.mapSceneId = readUShort().toInt()
            69 -> discard(1) // Unused.
            70 -> type.offsetX = readShort().toInt()
            71 -> type.offsetHeight = readShort().toInt()
            72 -> type.offsetY = readShort().toInt()
            73 -> type.boolean2 = true
            74 -> type.isSolid = true
            75 -> type.int3 = readUByte().toInt()
            77, 92 -> {
                type.transformVarbit = readUShort().toInt().let { if (it == 0xffff) -1 else it }
                type.transformVarp = readUShort().toInt().let { if (it == 0xffff) -1 else it }
                val prime = if (opcode == 77) -1 else readUShort().toInt().let { if (it == 0xffff) -1 else it }
                type.transforms = buildList {
                    repeat(readUByte().toInt() + 1) {
                        add(readUShort().toInt().let { if (it == 0xffff) -1 else it })
                    }
                    add(prime)
                }
            }
            78 -> {
                type.ambientSoundId = readUShort().toInt()
                type.int7 = readUByte().toInt()
            }
            79 -> {
                type.int5 = readUShort().toInt()
                type.int6 = readUShort().toInt()
                type.int7 = readUByte().toInt()
                repeat(readUByte().toInt()) {
                    discard(2) // Discard sound effect ids.
                }
            }
            81 -> type.clipType = readUByte().toInt() * 256
            82 -> type.mapIconId = readUShort().toInt()
            89 -> type.boolean3 = false
            249 -> type.params = readStringIntParameters()
            else -> throw IllegalArgumentException("Missing opcode $opcode.")
        }
        return loadEntryType(type)
    }
}
