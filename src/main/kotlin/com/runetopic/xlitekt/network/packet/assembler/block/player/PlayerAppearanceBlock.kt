package com.runetopic.xlitekt.network.packet.assembler.block.player

import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.game.actor.render.Render
import com.runetopic.xlitekt.network.packet.assembler.block.RenderingBlock
import com.runetopic.xlitekt.network.packet.assembler.block.player.kit.BodyPartColor
import com.runetopic.xlitekt.network.packet.assembler.block.player.kit.BodyPartCompanion
import com.runetopic.xlitekt.network.packet.assembler.block.player.kit.PlayerIdentityKit
import com.runetopic.xlitekt.util.ext.writeBytesAdd
import com.runetopic.xlitekt.util.ext.writeStringCp1252NullTerminated
import io.ktor.utils.io.core.BytePacketBuilder
import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.readBytes
import io.ktor.utils.io.core.writeShort

class PlayerAppearanceBlock : RenderingBlock<Player, Render.Appearance>(5, 0x1) {

    override fun build(actor: Player, render: Render.Appearance): ByteReadPacket = buildPacket {
        val data = buildPacket {
            writeByte(render.gender.mask.toByte())
            writeByte(render.skullIcon.toByte())
            writeByte(render.headIcon.toByte())
            if (render.transform != -1) writeTransmogrification(render) else writeIdentityKit(render)
            colour(this, render.bodyPartColors.entries)
            animate(render)
            writeStringCp1252NullTerminated(actor.displayName)
            writeByte(126) // Combat level
            writeShort(0) // Total level
            writeByte(0) // Hidden
        }
        writeByte(data.remaining.toByte())
        writeBytesAdd(data.readBytes())
    }

    private fun BytePacketBuilder.animate(render: Render.Appearance) {
        if (render.transform == -1) {
            intArrayOf(0x328, 0x337, 0x333, 0x334, 0x335, 0x336, 0x338).forEach { writeShort(it.toShort()) }
        } else {
            // TODO load npc defs for walking and stand anims for transmog.
        }
    }

    private fun BytePacketBuilder.writeTransmogrification(render: Render.Appearance) {
        writeShort(65535.toShort())
        writeShort(render.transform.toShort())
    }

    private fun BytePacketBuilder.writeIdentityKit(render: Render.Appearance) = PlayerIdentityKit.values()
        .sortedWith(compareBy { it.info.index })
        .forEach {
            it.info.build(
                this,
                BodyPartCompanion(
                    render.gender,
                    render.bodyParts.getOrDefault(it.bodyPart, 0)
                )
            )
        }

    private fun colour(builder: BytePacketBuilder, colours: Set<Map.Entry<BodyPartColor, Int>>) = colours
        .sortedWith(compareBy { it.key.id })
        .forEach { builder.writeByte(it.value.toByte()) }
}
