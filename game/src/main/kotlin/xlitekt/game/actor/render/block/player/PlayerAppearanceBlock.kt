package xlitekt.game.actor.render.block.player

import io.ktor.utils.io.core.BytePacketBuilder
import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.readBytes
import io.ktor.utils.io.core.writeShort
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.render.Render
import xlitekt.game.actor.render.block.RenderingBlock
import xlitekt.game.actor.render.block.player.kit.BodyPartColor
import xlitekt.game.actor.render.block.player.kit.PlayerIdentityKit
import xlitekt.shared.buffer.writeBytesAdd
import xlitekt.shared.buffer.writeStringCp1252NullTerminated

/**
 * @author Tyler Telis
 */
class PlayerAppearanceBlock : RenderingBlock<Player, Render.Appearance>(5, 0x1) {

    override fun build(actor: Player, render: Render.Appearance): ByteReadPacket = buildPacket {
        val data = buildPacket {
            writeByte(render.gender.mask.toByte())
            writeByte(render.skullIcon.toByte())
            writeByte(render.headIcon.toByte())
            if (render.transform != -1) writeTransmogrification(render) else writeIdentityKit(render)
            colour(render.bodyPartColors.entries)
            animate(render)
            writeStringCp1252NullTerminated(actor.username)
            writeByte(126) // Combat level
            writeShort(0) // Total level
            writeByte(0) // Hidden
        }
        writeByte(data.remaining.toByte())
        writeBytesAdd(data.readBytes())
    }

    private fun BytePacketBuilder.animate(render: Render.Appearance) = if (render.transform == -1) {
        shortArrayOf(808, 823, 819, 820, 821, 822, 824).forEach(::writeShort)
    } else {
        // TODO load npc defs for walking and stand anims for transmog.
    }

    private fun BytePacketBuilder.writeTransmogrification(render: Render.Appearance) {
        writeShort(65535.toShort())
        writeShort(render.transform.toShort())
    }

    private fun BytePacketBuilder.writeIdentityKit(render: Render.Appearance) = enumValues<PlayerIdentityKit>()
        .sortedBy { it.info.index }
        .forEach {
            // TODO We will need to add support for the item worn in the specific body slot.
            it.info.build(this, render.gender, render.bodyParts.getOrDefault(it.bodyPart, 0))
        }

    private fun BytePacketBuilder.colour(colours: Set<Map.Entry<BodyPartColor, Int>>) = colours.sortedBy { it.key.id }.forEach { writeByte(it.value.toByte()) }
}
