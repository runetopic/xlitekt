package xlitekt.cache.provider.config.obj

import xlitekt.cache.provider.EntryTypeProvider
import xlitekt.shared.buffer.discard
import xlitekt.shared.buffer.readByte
import xlitekt.shared.buffer.readInt
import xlitekt.shared.buffer.readStringCp1252NullTerminated
import xlitekt.shared.buffer.readUByte
import xlitekt.shared.buffer.readUShort
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
class ObjEntryTypeProvider : EntryTypeProvider<ObjEntryType>() {

    override fun load(): Map<Int, ObjEntryType> = store
        .index(CONFIG_INDEX)
        .group(OBJ_CONFIG)
        .files()
        .map { ByteBuffer.wrap(it.data).loadEntryType(ObjEntryType(it.id)) }
        .associateBy(ObjEntryType::id)

    override tailrec fun ByteBuffer.loadEntryType(type: ObjEntryType): ObjEntryType {
        when (val opcode = readUByte()) {
            0 -> { assertEmptyAndRelease(); return type }
            1 -> type.model = readUShort()
            2 -> type.name = readStringCp1252NullTerminated()
            4 -> type.zoom2d = readUShort()
            5 -> type.xan2d = readUShort()
            6 -> type.yan2d = readUShort()
            7 -> type.offsetX2d = readUShort().let { if (it > Short.MAX_VALUE) it - 65536 else it }
            8 -> type.offsetY2d = readUShort().let { if (it > Short.MAX_VALUE) it - 65536 else it }
            9 -> readStringCp1252NullTerminated() // Unused.
            11 -> type.isStackable = 1
            12 -> type.price = readInt()
            16 -> type.isMembersOnly = true
            23 -> {
                type.maleModel = readUShort()
                type.maleOffset = readUByte()
            }
            24 -> type.maleModel1 = readUShort()
            25 -> {
                type.femaleModel = readUShort()
                type.femaleOffset = readUByte()
            }
            26 -> type.femaleModel1 = readUShort()
            in 30..34 -> type.groundActions = type.groundActions.toMutableList().apply {
                this[opcode - 30] = readStringCp1252NullTerminated().let { if (it.equals("Hidden", true)) "null" else it }
            }
            in 35..39 -> type.inventoryActions = type.inventoryActions.toMutableList().apply {
                this[opcode - 35] = readStringCp1252NullTerminated()
            }
            40 -> repeat(readUByte()) {
                discard(4) // Discard recolor.
            }
            41 -> repeat(readUByte()) {
                discard(4) // Discard retexture.
            }
            42 -> type.shiftClickIndex = readByte()
            65 -> type.isTradable = true
            78 -> type.maleModel2 = readUShort()
            79 -> type.femaleModel2 = readUShort()
            90 -> type.maleHeadModel = readUShort()
            91 -> type.femaleHeadModel = readUShort()
            92 -> type.maleHeadModel2 = readUShort()
            93 -> type.femaleHeadModel2 = readUShort()
            94 -> discard(2) // Unused.
            95 -> type.zan2d = readUShort()
            97 -> type.note = readUShort()
            98 -> type.noteTemplate = readUShort()
            in 100..109 -> discard(4) // Discard countobj/countco.
            110 -> type.resizeX = readUShort()
            111 -> type.resizeY = readUShort()
            112 -> type.resizeZ = readUShort()
            113 -> type.ambient = readByte()
            114 -> type.contrast = readByte() * 5
            115 -> type.team = readUByte()
            139 -> type.unnotedId = readUShort()
            140 -> type.notedId = readUShort()
            148 -> type.placeholder = readUShort()
            149 -> type.placeholderTemplate = readUShort()
            249 -> type.params = readStringIntParameters()
            else -> throw IllegalArgumentException("Missing opcode $opcode.")
        }
        return loadEntryType(type)
    }

    override fun postLoadEntryType(type: ObjEntryType) {
        if (type.noteTemplate != -1) {
            entries[type.noteTemplate]?.let { noteTemplate ->
                entries[type.note]?.let { note ->
                    type.toNote(noteTemplate, note)
                }
            }
        }
        if (type.notedId != -1) {
            entries[type.notedId]?.let { noted ->
                entries[type.unnotedId]?.let { unnoted ->
                    type.toUnnoted(noted, unnoted)
                }
            }
        }
        if (type.placeholderTemplate != -1) {
            entries[type.placeholderTemplate]?.let { placeholderTemplate ->
                entries[type.placeholder]?.let { placeholder ->
                    type.toPlaceholder(placeholderTemplate, placeholder)
                }
            }
        }
    }

    private fun ObjEntryType.toNote(noteTemplate: ObjEntryType, note: ObjEntryType) {
        model = noteTemplate.model
        zoom2d = noteTemplate.zoom2d
        xan2d = noteTemplate.xan2d
        yan2d = noteTemplate.yan2d
        zan2d = noteTemplate.zan2d
        offsetX2d = noteTemplate.offsetX2d
        offsetY2d = noteTemplate.offsetY2d
        // Skip recolor.
        // Skip retexture.
        name = note.name
        isMembersOnly = note.isMembersOnly
        price = note.price
        isStackable = 1
    }

    private fun ObjEntryType.toUnnoted(noted: ObjEntryType, unnoted: ObjEntryType) {
        model = noted.model
        zoom2d = noted.zoom2d
        xan2d = noted.xan2d
        yan2d = noted.yan2d
        zan2d = noted.zan2d
        offsetX2d = noted.offsetX2d
        offsetY2d = noted.offsetY2d
        // Skip recolor.
        // Skip retexture.
        name = unnoted.name
        isMembersOnly = unnoted.isMembersOnly
        isStackable = unnoted.isStackable
        maleModel = unnoted.maleModel
        maleModel1 = unnoted.maleModel1
        maleModel2 = unnoted.maleModel2
        femaleModel = unnoted.femaleModel
        femaleModel1 = unnoted.femaleModel1
        femaleModel2 = unnoted.femaleModel2
        maleHeadModel = unnoted.maleHeadModel
        maleHeadModel2 = unnoted.maleHeadModel2
        femaleHeadModel = unnoted.femaleHeadModel
        femaleHeadModel2 = unnoted.femaleHeadModel2
        team = unnoted.team
        groundActions = unnoted.groundActions
        inventoryActions = buildList {
            repeat(3) {
                add(unnoted.inventoryActions[it])
            }
            add("Discard")
        }
        price = 0
    }

    private fun ObjEntryType.toPlaceholder(placeholderTemplate: ObjEntryType, placeholder: ObjEntryType) {
        model = placeholderTemplate.model
        zoom2d = placeholderTemplate.zoom2d
        xan2d = placeholderTemplate.xan2d
        yan2d = placeholderTemplate.yan2d
        zan2d = placeholderTemplate.zan2d
        offsetX2d = placeholderTemplate.offsetX2d
        offsetY2d = placeholderTemplate.offsetY2d
        // Skip recolor.
        // Skip retexture.
        isStackable = placeholderTemplate.isStackable
        name = placeholder.name
        price = 0
        isMembersOnly = false
        isTradable = false
    }
}
