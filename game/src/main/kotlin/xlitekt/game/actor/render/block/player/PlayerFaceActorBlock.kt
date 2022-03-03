package xlitekt.game.actor.render.block.player

import io.ktor.utils.io.core.buildPacket
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.render.Render
import xlitekt.game.actor.render.block.RenderingBlock
import xlitekt.shared.buffer.writeShortLittleEndianAdd

/**
 * @author Tyler Telis
 */
class PlayerFaceActorBlock : RenderingBlock<Player, Render.FaceActor>(4, 0x80) {
    override fun build(actor: Player, render: Render.FaceActor) = buildPacket {
        writeShortLittleEndianAdd(render.index.toShort())
    }
}
