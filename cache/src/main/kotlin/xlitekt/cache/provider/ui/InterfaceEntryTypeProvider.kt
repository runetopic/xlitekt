package xlitekt.cache.provider.ui

import xlitekt.cache.provider.EntryTypeProvider
import xlitekt.shared.buffer.discard
import xlitekt.shared.buffer.readByte
import xlitekt.shared.buffer.readInt
import xlitekt.shared.buffer.readShort
import xlitekt.shared.buffer.readStringCp1252NullTerminated
import xlitekt.shared.buffer.readUByte
import xlitekt.shared.buffer.readUMedium
import xlitekt.shared.buffer.readUShort
import xlitekt.shared.packInterface
import xlitekt.shared.toBoolean
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
class InterfaceEntryTypeProvider : EntryTypeProvider<InterfaceEntryType>() {

    override fun load(): Map<Int, InterfaceEntryType> = store
        .index(INTERFACE_INDEX)
        .groups()
        .flatMap { group ->
            group.files().map {
                ByteBuffer.wrap(it.data).loadEntryType(InterfaceEntryType(group.id.packInterface(it.id), isModern = it.data[0].toInt() == -1))
            }
        }
        .associateBy(InterfaceEntryType::id)

    override fun ByteBuffer.loadEntryType(type: InterfaceEntryType): InterfaceEntryType = type.apply {
        if (isModern) decodeModern(this) else decodeLegacy(this)
        assertEmptyAndRelease()
    }

    private fun ByteBuffer.decodeModern(type: InterfaceEntryType) {
        discard(1) // Unused.
        type.type = readUByte()
        type.contentType = readUShort()
        type.rawX = readShort()
        type.rawY = readShort()
        type.rawWidth = readUShort()
        type.rawHeight = if (type.type == 9) readShort() else readUShort()
        type.widthAlignment = readByte()
        type.heightAlignment = readByte()
        type.xAlignment = readByte()
        type.yAlignment = readByte()
        type.parentId = readUShort().let { if (it == 0xffff) -1 else it + type.id and 0xffff.inv() }
        type.isHidden = readUByte().toBoolean()

        when (type.type) {
            0 -> {
                type.scrollWidth = readUShort()
                type.scrollHeight = readUShort()
                type.noClickThrough = readUByte().toBoolean()
            }
            3 -> {
                type.color = readInt()
                type.fill = readUByte().toBoolean()
                type.transparencyTop = readUByte()
            }
            4 -> {
                type.fontId = readUShort().let { if (it == 0xffff) -1 else it }
                type.text = readStringCp1252NullTerminated()
                type.textLineHeight = readUByte()
                type.textXAlignment = readUByte()
                type.textYAlignment = readUByte()
                type.textShadowed = readUByte().toBoolean()
                type.color = readInt()
            }
            5 -> {
                type.spriteId2 = readInt()
                type.spriteAngle = readUShort()
                type.spriteTiling = readUByte().toBoolean()
                type.transparencyTop = readUByte()
                type.outline = readUByte()
                type.spriteShadow = readInt()
                type.spriteFlipV = readUByte().toBoolean()
                type.spriteFlipH = readUByte().toBoolean()
            }
            6 -> {
                type.modelType = 1
                type.modelId = readUShort().let { if (it == 0xffff) -1 else it }
                type.modelOffsetX = readShort()
                type.modelOffsetY = readShort()
                type.modelAngleX = readUShort()
                type.modelAngleY = readUShort()
                type.modelAngleZ = readUShort()
                type.modelZoom = readUShort()
                type.sequenceId = readUShort().let { if (it == 0xffff) -1 else it }
                type.modelOrthog = readUByte().toBoolean()
                discard(2) // Unused.
                if (type.widthAlignment != 0) type.field3280 = readUShort()
                if (type.heightAlignment != 0) discard(2) // Unused.
            }
            9 -> {
                type.lineWid = readUByte()
                type.color = readInt()
                type.field3359 = readUByte().toBoolean()
            }
        }
        type.flags = readUMedium()
        type.dataText = readStringCp1252NullTerminated()

        type.actions = buildList {
            repeat(readUByte()) {
                add(readStringCp1252NullTerminated())
            }
        }

        type.dragZoneSize = readUByte()
        type.dragThreshold = readUByte()
        type.isScrollBar = readUByte().toBoolean()
        type.spellActionName = readStringCp1252NullTerminated()
        discard(remaining()) // Discard the remaining buffer for the listeners.
    }

    private fun ByteBuffer.decodeLegacy(type: InterfaceEntryType) {
        type.type = readUByte()
        type.buttonType = readUByte()
        type.contentType = readUShort()
        type.rawX = readShort()
        type.rawY = readShort()
        type.rawWidth = readUShort()
        type.rawHeight = readUShort()
        type.transparencyTop = readUByte()
        type.parentId = readUShort().let { if (it == 0xffff) -1 else it + type.id and 0xffff.inv() }
        type.mouseOverRedirect = readUShort().let { if (it == 0xffff) -1 else it }

        repeat(readUByte()) { // cs1 comparisons/values.
            discard(3) // Discard the cs1 comparisons and values.
        }

        repeat(readUByte()) { // cs1 instructions.
            repeat(readUShort()) {
                discard(2) // Discard the cs1 instructions values.
            }
        }

        when (type.type) {
            0 -> {
                type.scrollHeight = readUShort()
                type.isHidden = readUByte().toBoolean()
            }
            1 -> discard(3) // Unused.
            2 -> {
                type.itemIds = List(type.rawWidth * type.rawHeight) { 0 }
                type.itemQuantities = List(type.rawWidth * type.rawHeight) { 0 }
                discard(4) // Discard flags.
                type.paddingX = readUByte()
                type.paddingY = readUByte()

                repeat(20) {
                    if (readUByte() == 1) {
                        discard(8) // Discard inventory sprites and offsets.
                    }
                }

                repeat(5) {
                    readStringCp1252NullTerminated() // Discard item actions.
                }

                type.spellActionName = readStringCp1252NullTerminated()
                type.spellName = readStringCp1252NullTerminated()
                discard(2) // Discard flags.
            }
            3 -> type.fill = readUByte().toBoolean()
            5 -> {
                type.spriteId2 = readInt()
                type.spriteId = readInt()
            }
            6 -> {
                type.modelType = 1
                type.modelId = readUShort().let { if (it == 0xffff) -1 else it }

                type.modelType2 = 1
                type.modelId2 = readUShort().let { if (it == 0xffff) -1 else it }

                type.sequenceId = readUShort().let { if (it == 0xffff) -1 else it }
                type.sequenceId2 = readUShort().let { if (it == 0xffff) -1 else it }
                type.modelZoom = readUShort()
                type.modelAngleX = readUShort()
                type.modelAngleY = readUShort()
            }
            7 -> {
                type.itemIds = List(type.rawWidth * type.rawHeight) { 0 }
                type.itemQuantities = List(type.rawWidth * type.rawHeight) { 0 }
                type.textXAlignment = readUByte()
                type.fontId = readUShort().let { if (it == 0xffff) -1 else it }
                type.textShadowed = readUByte().toBoolean()
                type.color = readInt()
                type.paddingX = readShort()
                type.paddingY = readShort()
                discard(1) // Discard flags.

                repeat(5) {
                    readStringCp1252NullTerminated() // Discard item actions.
                }
            }
            8 -> type.text = readStringCp1252NullTerminated()
        }

        if (type.type == 1 || type.type == 3 || type.type == 4) {
            if (type.type != 3) {
                type.textXAlignment = readUByte()
                type.textYAlignment = readUByte()
                type.textLineHeight = readUByte()
                type.fontId = readUShort().let { if (it == 0xffff) -1 else it }
                type.textShadowed = readUByte().toBoolean()
                if (type.type == 4) {
                    type.text = readStringCp1252NullTerminated()
                    type.text2 = readStringCp1252NullTerminated()
                }
            }
            type.color = readInt()
            if (type.type != 1) {
                type.color2 = readInt()
                type.mouseOverColor = readInt()
                type.mouseOverColor2 = readInt()
            }
        }

        when (type.buttonType) {
            2 -> {
                type.spellActionName = readStringCp1252NullTerminated()
                type.spellName = readStringCp1252NullTerminated()
                discard(2) // Discard flags.
            }
            1, 4, 5, 6 -> {
                type.buttonText = readStringCp1252NullTerminated()
                if (type.buttonText.isEmpty()) {
                    type.buttonText = when (type.buttonType) {
                        1 -> "Ok"
                        4, 5 -> "Select"
                        6 -> "Continue"
                        else -> throw IllegalArgumentException("Could not set text for button type ${type.buttonType}.")
                    }
                }
            }
        }
    }
}
