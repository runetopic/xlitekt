package xlitekt.game.actor.render.block.player

import io.ktor.utils.io.core.buildPacket
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.render.Render
import xlitekt.game.actor.render.block.RenderingBlock

class PlayerTemporaryMovementTypeBlock : RenderingBlock<Player, Render.TemporaryMovementType>(2, 0x2000) {
    override fun build(actor: Player, render: Render.TemporaryMovementType) = buildPacket {
        writeByte(render.id.toByte())
    }
}
