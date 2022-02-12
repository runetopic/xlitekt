package com.runetopic.xlitekt.cache.provider.ui

import com.runetopic.xlitekt.cache.provider.EntryTypeProvider
import com.runetopic.xlitekt.util.ext.packInterface
import com.runetopic.xlitekt.util.ext.readMedium
import com.runetopic.xlitekt.util.ext.readStringCp1252NullTerminated
import com.runetopic.xlitekt.util.ext.toBoolean
import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readInt
import io.ktor.utils.io.core.readShort
import io.ktor.utils.io.core.readUByte
import io.ktor.utils.io.core.readUShort

/**
 * @author Jordan Abraham
 */
class InterfaceEntryTypeProvider : EntryTypeProvider<InterfaceEntryType>() {

    override fun load(): List<InterfaceEntryType> {
        return store.index(INTERFACE_INDEX).groups().flatMap { group ->
            group.files().map {
                loadEntryType(
                    ByteReadPacket(it.data),
                    InterfaceEntryType(id = group.id.packInterface(it.id), isModern = it.data[0].toInt() == -1)
                )
            }
        }
    }

    override fun loadEntryType(buffer: ByteReadPacket, type: InterfaceEntryType): InterfaceEntryType {
        buffer.apply { if (type.isModern) decodeModern(type) else decodeLegacy(type) }
        return type
    }

    private fun ByteReadPacket.decodeModern(type: InterfaceEntryType) {
        discard(1) // Unused.
        type.type = readUByte().toInt()
        type.contentType = readUShort().toInt()
        type.rawX = readShort().toInt()
        type.rawY = readShort().toInt()
        type.rawWidth = readUShort().toInt()
        type.rawHeight = if (type.type == 9) readShort().toInt() else readUShort().toInt()
        type.widthAlignment = readByte().toInt()
        type.heightAlignment = readByte().toInt()
        type.xAlignment = readByte().toInt()
        type.yAlignment = readByte().toInt()
        type.parentId = readUShort().toInt().let { if (it == 65535) -1 else it + type.id and -65536 }
        type.isHidden = readUByte().toInt().toBoolean()

        when (type.type) {
            0 -> {
                type.scrollWidth = readUShort().toInt()
                type.scrollHeight = readUShort().toInt()
                type.noClickThrough = readUByte().toInt().toBoolean()
            }
            3 -> {
                type.color = readInt()
                type.fill = readUByte().toInt().toBoolean()
                type.transparencyTop = readUByte().toInt()
            }
            4 -> {
                type.fontId = readUShort().toInt().let { if (it == 65535) -1 else it }
                type.text = readStringCp1252NullTerminated()
                type.textLineHeight = readUByte().toInt()
                type.textXAlignment = readUByte().toInt()
                type.textYAlignment = readUByte().toInt()
                type.textShadowed = readUByte().toInt().toBoolean()
                type.color = readInt()
            }
            5 -> {
                type.spriteId2 = readInt()
                type.spriteAngle = readUShort().toInt()
                type.spriteTiling = readUByte().toInt().toBoolean()
                type.transparencyTop = readUByte().toInt()
                type.outline = readUByte().toInt()
                type.spriteShadow = readInt()
                type.spriteFlipV = readUByte().toInt().toBoolean()
                type.spriteFlipH = readUByte().toInt().toBoolean()
            }
            6 -> {
                type.modelType = 1
                type.modelId = readUShort().toInt().let { if (it == 65535) -1 else it }
                type.modelOffsetX = readShort().toInt()
                type.modelOffsetY = readShort().toInt()
                type.modelAngleX = readUShort().toInt()
                type.modelAngleY = readUShort().toInt()
                type.modelAngleZ = readUShort().toInt()
                type.modelZoom = readUShort().toInt()
                type.sequenceId = readUShort().toInt().let { if (it == 65535) -1 else it }
                type.modelOrthog = readUByte().toInt().toBoolean()
                discard(2) // Unused.
                if (type.widthAlignment != 0) type.field3280 = readUShort().toInt()
                if (type.heightAlignment != 0) discard(2) // Unused.
            }
            9 -> {
                type.lineWid = readUByte().toInt()
                type.color = readInt()
                type.field3359 = readUByte().toInt().toBoolean()
            }
        }
        type.flags = readMedium()
        type.dataText = readStringCp1252NullTerminated()

        val actionsSize = readUByte().toInt()
        if (actionsSize > 0) {
            type.actions = buildList {
                repeat(actionsSize) {
                    add(readStringCp1252NullTerminated())
                }
            }
        }

        type.dragZoneSize = readUByte().toInt()
        type.dragThreshold = readUByte().toInt()
        type.isScrollBar = readUByte().toInt().toBoolean()
        type.spellActionName = readStringCp1252NullTerminated()
        discard(remaining) // Discard the remaining buffer for the listeners.
        release()
    }

    private fun ByteReadPacket.decodeLegacy(type: InterfaceEntryType) {
        type.type = readUByte().toInt()
        type.buttonType = readUByte().toInt()
        type.contentType = readUShort().toInt()
        type.rawX = readShort().toInt()
        type.rawY = readShort().toInt()
        type.rawWidth = readUShort().toInt()
        type.rawHeight = readUShort().toInt()
        type.transparencyTop = readUByte().toInt()
        type.parentId = readUShort().toInt().let { if (it == 65535) -1 else it + type.id and -65536 }
        type.mouseOverRedirect = readUShort().toInt().let { if (it == 65535) -1 else it }

        val cs1ComparisonsSize = readUByte().toInt()
        repeat(cs1ComparisonsSize) {
            discard(3) // Discard the cs1 comparisons and values.
        }

        val cs1InstructionsSize = readUByte().toInt()
        repeat(cs1InstructionsSize) {
            repeat(readUShort().toInt()) {
                discard(2) // Discard the cs1 instructions values.
            }
        }

        if (type.type == 0) {
            type.scrollHeight = readUShort().toInt()
            type.isHidden = readUByte().toInt().toBoolean()
        }

        if (type.type == 1) {
            discard(3) // Unused.
        }

        if (type.type == 2) {
            type.itemIds = List(type.rawWidth * type.rawHeight) { 0 }
            type.itemQuantities = List(type.rawWidth * type.rawHeight) { 0 }
            discard(4) // Dicard flags.
            type.paddingX = readUByte().toInt()
            type.paddingY = readUByte().toInt()

            repeat(20) {
                if (readUByte().toInt() == 1) {
                    discard(8) // Discard inventory sprites and offsets.
                }
            }

            repeat(5) {
                readStringCp1252NullTerminated() // Discard item actions.
            }
        }

        if (type.type == 3) {
            type.fill = readUByte().toInt().toBoolean()
        }

        if (type.type == 4 || type.type == 1) {
            type.textXAlignment = readUByte().toInt()
            type.textYAlignment = readUByte().toInt()
            type.textLineHeight = readUByte().toInt()
            type.fontId = readUShort().toInt().let { if (it == 65535) -1 else it }
            type.textShadowed = readUByte().toInt().toBoolean()
        }

        if (type.type == 4) {
            type.text = readStringCp1252NullTerminated()
            type.text2 = readStringCp1252NullTerminated()
        }

        if (type.type == 1 || type.type == 3 || type.type == 4) {
            type.color = readInt()
        }

        if (type.type == 3 || type.type == 4) {
            type.color2 = readInt()
            type.mouseOverColor = readInt()
            type.mouseOverColor2 = readInt()
        }

        if (type.type == 5) {
            type.spriteId2 = readInt()
            type.spriteId = readInt()
        }

        if (type.type == 6) {
            type.modelType = 1
            type.modelId = readUShort().toInt().let { if (it == 65535) -1 else it }

            type.modelType2 = 1
            type.modelId2 = readUShort().toInt().let { if (it == 65535) -1 else it }

            type.sequenceId = readUShort().toInt().let { if (it == 65535) -1 else it }
            type.sequenceId2 = readUShort().toInt().let { if (it == 65535) -1 else it }
            type.modelZoom = readUShort().toInt()
            type.modelAngleX = readUShort().toInt()
            type.modelAngleY = readUShort().toInt()
        }

        if (type.type == 7) {
            type.itemIds = List(type.rawWidth * type.rawHeight) { 0 }
            type.itemQuantities = List(type.rawWidth * type.rawHeight) { 0 }
            type.textXAlignment = readUByte().toInt()
            type.fontId = readUShort().toInt().let { if (it == 65535) -1 else it }
            type.textShadowed = readUByte().toInt().toBoolean()
            type.color = readInt()
            type.paddingX = readShort().toInt()
            type.paddingY = readShort().toInt()
            discard(1) // Discard flags.

            repeat(5) {
                readStringCp1252NullTerminated() // Discard item actions.
            }
        }

        if (type.type == 8) {
            type.text = readStringCp1252NullTerminated()
        }

        if (type.buttonType == 2 || type.type == 2) {
            type.spellActionName = readStringCp1252NullTerminated()
            type.spellName = readStringCp1252NullTerminated()
            discard(2) // Discard flags.
        }

        if (type.buttonType == 1 || type.buttonType == 4 || type.buttonType == 5 || type.buttonType == 6) {
            type.buttonText = readStringCp1252NullTerminated()
            if (type.buttonText.isEmpty()) {
                type.buttonText = when (type.buttonType) {
                    1 -> "Ok"
                    4 -> "Select"
                    5 -> "Select"
                    6 -> "Continue"
                    else -> throw IllegalArgumentException("Could not set text for button type ${type.buttonType}.")
                }
            }
        }
        release()
    }
}
