package com.runetopic.xlitekt.cache.provider.config.obj

import com.runetopic.xlitekt.cache.provider.EntryTypeProvider
import com.runetopic.xlitekt.util.ext.readStringCp1252NullTerminated
import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readInt
import io.ktor.utils.io.core.readUByte
import io.ktor.utils.io.core.readUShort

/**
 * @author Jordan Abraham
 */
class ObjEntryTypeProvider : EntryTypeProvider<ObjEntryType>() {

    override fun load(): Set<ObjEntryType> = store
        .index(CONFIG_INDEX)
        .group(OBJ_CONFIG)
        .files()
        .map { ByteReadPacket(it.data).loadEntryType(ObjEntryType(it.id)) }
        .toHashSet()

    override fun ByteReadPacket.loadEntryType(type: ObjEntryType): ObjEntryType {
        do when (val opcode = readUByte().toInt()) {
            0 -> break
            1 -> type.model = readUShort().toInt()
            2 -> type.name = readStringCp1252NullTerminated()
            4 -> type.zoom2d = readUShort().toInt()
            5 -> type.xan2d = readUShort().toInt()
            6 -> type.yan2d = readUShort().toInt()
            7 -> type.offsetX2d = readUShort().toInt().let { if (it > Short.MAX_VALUE) it - 65536 else it }
            8 -> type.offsetY2d = readUShort().toInt().let { if (it > Short.MAX_VALUE) it - 65536 else it }
            9 -> readStringCp1252NullTerminated() // Unused.
            11 -> type.isStackable = 1
            12 -> type.price = readInt()
            16 -> type.isMembersOnly = true
            23 -> {
                type.maleModel = readUShort().toInt()
                type.maleOffset = readUByte().toInt()
            }
            24 -> type.maleModel1 = readUShort().toInt()
            25 -> {
                type.femaleModel = readUShort().toInt()
                type.femaleOffset = readUByte().toInt()
            }
            26 -> type.femaleModel1 = readUShort().toInt()
            in 30..34 -> type.groundActions = type.groundActions.toMutableList().apply {
                this[opcode - 30] = readStringCp1252NullTerminated().let { if (it.equals("Hidden", true)) "null" else it }
            }
            in 35..39 -> type.inventoryActions = type.inventoryActions.toMutableList().apply {
                this[opcode - 35] = readStringCp1252NullTerminated()
            }
            40 -> repeat(readUByte().toInt()) {
                discard(4) // Discard recolor.
            }
            41 -> repeat(readUByte().toInt()) {
                discard(4) // Discard retexture.
            }
            42 -> type.shiftClickIndex = readByte().toInt()
            65 -> type.isTradable = true
            78 -> type.maleModel2 = readUShort().toInt()
            79 -> type.femaleModel2 = readUShort().toInt()
            90 -> type.maleHeadModel = readUShort().toInt()
            91 -> type.femaleHeadModel = readUShort().toInt()
            92 -> type.maleHeadModel2 = readUShort().toInt()
            93 -> type.femaleHeadModel2 = readUShort().toInt()
            94 -> discard(2) // Unused.
            95 -> type.zan2d = readUShort().toInt()
            97 -> type.note = readUShort().toInt()
            98 -> type.noteTemplate = readUShort().toInt()
            in 100..109 -> discard(4) // Discard countobj/countco.
            110 -> type.resizeX = readUShort().toInt()
            111 -> type.resizeY = readUShort().toInt()
            112 -> type.resizeZ = readUShort().toInt()
            113 -> type.ambient = readByte().toInt()
            114 -> type.contrast = readByte() * 5
            115 -> type.team = readUByte().toInt()
            139 -> type.unnotedId = readUShort().toInt()
            140 -> type.notedId = readUShort().toInt()
            148 -> type.placeholder = readUShort().toInt()
            149 -> type.placeholderTemplate = readUShort().toInt()
            249 -> type.params = readStringIntParameters()
        } while (true)
        assertEmptyThenRelease()
        return type
    }
}
