package xlitekt.game.actor.render.block.player

import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.buildPacket
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.render.Render
import xlitekt.game.actor.render.block.RenderingBlock
import xlitekt.shared.buffer.writeByteNegate
import xlitekt.shared.buffer.writeShortAdd

/**
 * @author Tyler Telis
 */
class PlayerSequenceBlock : RenderingBlock<Player, Render.Sequence>(8, 0x2) {

    override fun build(actor: Player, render: Render.Sequence): ByteReadPacket = buildPacket {
        writeShortAdd(render.id.toShort())
        writeByteNegate(render.delay.toByte())
    }
}
