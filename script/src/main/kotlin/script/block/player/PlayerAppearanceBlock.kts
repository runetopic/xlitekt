package script.block.player

import xlitekt.game.actor.render.Render.Appearance
import xlitekt.game.actor.render.block.body.BodyPartBuilder
import xlitekt.game.actor.render.block.body.BodyPartColor
import xlitekt.game.actor.render.block.body.BodyPartInfo
import xlitekt.game.actor.render.block.body.BodyPartListener
import xlitekt.game.actor.render.block.onPlayerUpdateBlock
import xlitekt.shared.buffer.allocate
import xlitekt.shared.buffer.allocateDynamic
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.buffer.writeBytes
import xlitekt.shared.buffer.writeBytesAdd
import xlitekt.shared.buffer.writeShort
import xlitekt.shared.buffer.writeStringCp1252NullTerminated
import xlitekt.shared.inject
import xlitekt.shared.resource.ItemSequences
import java.nio.ByteBuffer

val itemSequences by inject<ItemSequences>()

/**
 * @author Jordan Abraham
 */
onPlayerUpdateBlock<Appearance>(5, 0x1) {
    val appearance = allocateDynamic(100) {
        writeByte(gender.id)
        writeByte(skullIcon.orElse(-1))
        writeByte(headIcon.orElse(-1))
        if (transform.isPresent) writeTransmogrification(this@onPlayerUpdateBlock) else writeIdentityKit(this@onPlayerUpdateBlock)
        color(bodyPartColors.entries)
        animate(this@onPlayerUpdateBlock)
        writeStringCp1252NullTerminated(displayName)
        writeByte(126) // Combat level
        writeShort(0) // Total level
        writeByte(0) // Hidden
    }
    allocate(appearance.size + 1) {
        writeByte(appearance.size)
        writeBytesAdd(appearance)
    }
}

fun ByteBuffer.animate(render: Appearance) = if (render.transform.isEmpty) {
    val sequences = itemSequences[render.equipment.mainhand?.id]?.renderAnimations ?: intArrayOf(808, 823, 819, 820, 821, 822, 824)
    sequences.forEach(::writeShort)
} else {
    // TODO load npc defs for walking and stand anims for transmog.
}

fun ByteBuffer.writeTransmogrification(render: Appearance) {
    writeShort(65535)
    writeShort(render.transform.get())
}

fun ByteBuffer.writeIdentityKit(render: Appearance) {
    BodyPartListener.listeners.entries.sortedBy(MutableMap.MutableEntry<Int, BodyPartInfo>::key).forEach {
        val bodyPartBuilder = BodyPartBuilder(render.bodyParts.getOrDefault(it.value.bodyPart, 0), render.gender, render.equipment)
        it.value.builder.invoke(bodyPartBuilder)
        writeBytes(bodyPartBuilder.data ?: byteArrayOf())
    }
}

fun ByteBuffer.color(colors: Set<Map.Entry<BodyPartColor, Int>>) = colors.sortedBy { it.key.id }.forEach { writeByte(it.value) }
