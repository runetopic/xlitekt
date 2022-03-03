package xlitekt.game.actor.render.block.player

import io.ktor.utils.io.core.buildPacket
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.render.Render
import xlitekt.game.actor.render.block.RenderingBlock
import xlitekt.shared.buffer.writeIntV2
import xlitekt.shared.buffer.writeShortAdd

/**
 * @author Tyler Telis
 */
class PlayerSpotAnimationBlock : RenderingBlock<Player, Render.SpotAnimation>(10, 0x800) {
    override fun build(actor: Player, render: Render.SpotAnimation) = buildPacket {
        writeShortAdd(render.id.toShort())
        writeIntV2(render.packedMetaData())
    }
}
