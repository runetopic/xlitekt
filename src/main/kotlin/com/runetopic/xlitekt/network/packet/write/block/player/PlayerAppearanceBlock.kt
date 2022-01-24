package com.runetopic.xlitekt.network.packet.write.block.player

import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.game.actor.render.Render
import com.runetopic.xlitekt.network.packet.write.block.RenderingBlock
import com.runetopic.xlitekt.network.packet.write.block.player.kit.BodyPartColor
import com.runetopic.xlitekt.network.packet.write.block.player.kit.BodyPartCompanion
import com.runetopic.xlitekt.network.packet.write.block.player.kit.PlayerIdentityKit
import com.runetopic.xlitekt.util.ext.writeBytesAdd
import com.runetopic.xlitekt.util.ext.writeString
import io.ktor.utils.io.core.BytePacketBuilder
import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.readBytes
import io.ktor.utils.io.core.writeShort

class PlayerAppearanceBlock : RenderingBlock<Player, Render.Appearance> {

    override fun keyPair(): Pair<Int, Int> = Pair(5, 0x1)

    override fun build(actor: Player, render: Render.Appearance): ByteReadPacket = buildPacket {
        val data = buildPacket {
            writeByte(render.gender.mask.toByte())
            writeByte(render.skullIcon.toByte())
            writeByte(render.headIcon.toByte())
            if (render.transform != -1) {
                buildTransmogrification(this, render)
            } else {
                buildIdentityKit(this, render)
            }
            colour(this, render.bodyPartColors.entries)
            if (render.transform == -1) {
                intArrayOf(0x328, 0x337, 0x333, 0x334, 0x335, 0x336, 0x338).forEach { writeShort(it.toShort()) }
            } else {
                // TODO load npc defs for walking and stand anims for transmog.
            }
            writeString(actor.displayName)
            writeByte(126) // Combat level
            writeShort(0) // Total level
            writeByte(0) // Hidden
        }
        writeByte(data.remaining.toByte())
        writeBytesAdd(data.readBytes())
    }

    private fun buildTransmogrification(builder: BytePacketBuilder, render: Render.Appearance) {
        builder.writeShort(65535.toShort())
        builder.writeShort(render.transform.toShort())
    }

    private fun buildIdentityKit(builder: BytePacketBuilder, render: Render.Appearance) = PlayerIdentityKit.values()
        .sortedWith(compareBy { it.info.index() })
        .forEach {
            it.info.build(
                builder,
                BodyPartCompanion(
                    render.gender,
                    render.bodyParts.getOrDefault(it.bodyPart, 0)
                )
            )
        }

    private fun colour(builder: BytePacketBuilder, colours: Set<Map.Entry<BodyPartColor, Int>>) = colours
        .sortedWith(compareBy { it.key.id })
        .forEach { (value) -> builder.writeByte(value.color.toByte()) }
}
