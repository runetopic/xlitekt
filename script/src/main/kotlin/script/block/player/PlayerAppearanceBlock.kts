package script.block.player

import io.ktor.utils.io.core.BytePacketBuilder
import io.ktor.utils.io.core.writeFully
import io.ktor.utils.io.core.writeShort
import xlitekt.game.actor.render.Render.Appearance
import xlitekt.game.actor.render.block.PlayerRenderingBlockListener
import xlitekt.game.actor.render.block.body.BodyPartBlock
import xlitekt.game.actor.render.block.body.BodyPartBlockListener
import xlitekt.game.actor.render.block.body.BodyPartBuilder
import xlitekt.game.actor.render.block.body.BodyPartColor
import xlitekt.shared.buffer.dynamicBuffer
import xlitekt.shared.buffer.writeBytesAdd
import xlitekt.shared.buffer.writeStringCp1252NullTerminated
import xlitekt.shared.inject
import xlitekt.shared.insert
import xlitekt.shared.resource.ItemSequences

/**
 * @author Jordan Abraham
 */
val itemSequences by inject<ItemSequences>()
val bodyPartBlockListener by inject<BodyPartBlockListener>()

insert<PlayerRenderingBlockListener>().dynamicPlayerUpdateBlock<Appearance>(index = 5, mask = 0x1, size = -1) {
    val appearance = dynamicBuffer {
        writeByte(gender.id.toByte())
        writeByte(skullIcon.orElse(-1).toByte())
        writeByte(headIcon.orElse(-1).toByte())
        if (transform.isPresent) writeTransmogrification(this@dynamicPlayerUpdateBlock) else writeIdentityKit(this@dynamicPlayerUpdateBlock)
        color(bodyPartColors.entries)
        animate(this@dynamicPlayerUpdateBlock)
        writeStringCp1252NullTerminated(displayName)
        writeByte(126) // Combat level
        writeShort(0) // Total level
        writeByte(0) // Hidden
    }
    it.writeByte(appearance.size.toByte())
    it.writeBytesAdd(appearance)
}

fun BytePacketBuilder.animate(render: Appearance) = if (render.transform.isEmpty) {
    val sequences = itemSequences[render.equipment.mainhand?.id]?.renderAnimations ?: intArrayOf(808, 823, 819, 820, 821, 822, 824)
    sequences.map(Int::toShort).forEach(::writeShort)
} else {
    // TODO load npc defs for walking and stand anims for transmog.
}

fun BytePacketBuilder.writeTransmogrification(render: Appearance) {
    writeShort(65535.toShort())
    writeShort(render.transform.get().toShort())
}

fun BytePacketBuilder.writeIdentityKit(render: Appearance) {
    bodyPartBlockListener.entries.sortedBy(MutableMap.MutableEntry<Int, BodyPartBlock>::key).forEach {
        val bodyPartBuilder = BodyPartBuilder(render.bodyParts.getOrDefault(it.value.bodyPart, 0), render.gender, render.equipment)
        it.value.builder.invoke(bodyPartBuilder)
        val block = bodyPartBuilder.data
        if (block != null) writeFully(block)
    }
}

fun BytePacketBuilder.color(colors: Set<Map.Entry<BodyPartColor, Int>>) = colors.sortedBy { it.key.id }.forEach { writeByte(it.value.toByte()) }
