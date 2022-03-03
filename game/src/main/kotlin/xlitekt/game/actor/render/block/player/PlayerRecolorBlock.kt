package xlitekt.game.actor.render.block.player

import io.ktor.utils.io.core.buildPacket
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.render.Render
import xlitekt.game.actor.render.block.RenderingBlock
import xlitekt.shared.buffer.writeByteSubtract
import xlitekt.shared.buffer.writeShortAdd
import xlitekt.shared.buffer.writeShortLittleEndianAdd

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
