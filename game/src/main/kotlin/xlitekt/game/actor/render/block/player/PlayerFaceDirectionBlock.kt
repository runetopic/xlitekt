package xlitekt.game.actor.render.block.player

import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeShortLittleEndian
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.render.Render
import xlitekt.game.actor.render.block.RenderingBlock

/**
 * @author Tyler Telis
 */
class PlayerFaceDirectionBlock : RenderingBlock<Player, Render.FaceDirection>(12, 0x40) {
    override fun build(actor: Player, render: Render.FaceDirection) = buildPacket {
        writeShortLittleEndian(render.direction.toShort())
    }
}
