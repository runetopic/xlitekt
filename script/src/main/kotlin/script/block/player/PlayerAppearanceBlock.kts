package script.block.player

import io.ktor.utils.io.core.BytePacketBuilder
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.readBytes
import xlitekt.game.actor.render.Render.Appearance
import xlitekt.game.actor.render.block.body.BodyPartBuilder
import xlitekt.game.actor.render.block.body.BodyPartColor
import xlitekt.game.actor.render.block.body.BodyPartInfo
import xlitekt.game.actor.render.block.body.BodyPartListener
import xlitekt.game.actor.render.block.onPlayerUpdateBlock
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.buffer.writeBytes
import xlitekt.shared.buffer.writeBytesAdd
import xlitekt.shared.buffer.writeShort
import xlitekt.shared.buffer.writeStringCp1252NullTerminated

/**
 * @author Jordan Abraham
 */
onPlayerUpdateBlock<Appearance>(5, 0x1) {
    buildPacket {
        val appearance = buildPacket {
            writeByte(gender::mask)
            writeByte { skullIcon.orElse(-1) }
            writeByte { headIcon.orElse(-1) }
            if (transform.isPresent) writeTransmogrification(this@onPlayerUpdateBlock) else writeIdentityKit(this@onPlayerUpdateBlock)
            color(bodyPartColors.entries)
            animate(this@onPlayerUpdateBlock)
            writeStringCp1252NullTerminated { displayName }
            writeByte { 126 } // Combat level
            writeShort { 0 } // Total level
            writeByte { 0 } // Hidden
        }
        writeByte(appearance.remaining::toInt)
        writeBytesAdd(appearance::readBytes)
        appearance.release()
    }
}

fun BytePacketBuilder.animate(render: Appearance) = if (render.transform.isEmpty) {
    intArrayOf(808, 823, 819, 820, 821, 822, 824).forEach { writeShort { it } }
} else {
    // TODO load npc defs for walking and stand anims for transmog.
}

fun BytePacketBuilder.writeTransmogrification(render: Appearance) {
    writeShort { 65535 }
    writeShort(render.transform::get)
}

fun BytePacketBuilder.writeIdentityKit(render: Appearance) {
    BodyPartListener.listeners.entries.sortedBy(MutableMap.MutableEntry<Int, BodyPartInfo>::key).forEach {
        // TODO We will need to add support for the item worn in the specific body slot.
        val bodyPartBuilder = BodyPartBuilder(render.bodyParts.getOrDefault(it.value.bodyPart, 0), render.gender, render.equipment)
        it.value.builder.invoke(bodyPartBuilder)
        writeBytes { bodyPartBuilder.data!! }
    }
}

fun BytePacketBuilder.color(colours: Set<Map.Entry<BodyPartColor, Int>>) = colours.sortedBy { it.key.id }.forEach { writeByte(it::value) }
