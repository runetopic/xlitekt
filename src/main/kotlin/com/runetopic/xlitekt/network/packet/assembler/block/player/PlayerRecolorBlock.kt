package com.runetopic.xlitekt.network.packet.assembler.block.player

import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.game.actor.render.Render
import com.runetopic.xlitekt.network.packet.assembler.block.RenderingBlock
import com.runetopic.xlitekt.shared.buffer.writeByteSubtract
import com.runetopic.xlitekt.shared.buffer.writeShortAdd
import com.runetopic.xlitekt.shared.buffer.writeShortLittleEndianAdd
import io.ktor.utils.io.core.buildPacket

/**
 * @author Tyler Telis
 */
class PlayerRecolorBlock : RenderingBlock<Player, Render.Recolor>(11, 0x200) {
    override fun build(actor: Player, render: Render.Recolor) = buildPacket {
        writeShortLittleEndianAdd(render.startDelay.toShort())
        writeShortAdd(render.endDelay.toShort())
        writeByte(render.hue.toByte())
        writeByteSubtract(render.saturation.toByte())
        writeByte(render.luminance.toByte())
        writeByte(render.amount.toByte())
    }
}
