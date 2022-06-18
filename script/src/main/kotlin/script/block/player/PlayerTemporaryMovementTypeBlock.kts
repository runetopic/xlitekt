package script.block.player

import xlitekt.game.actor.render.Render.TemporaryMovementType
import xlitekt.game.actor.render.block.PlayerRenderingBlockListener
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.insert

/**
 * @author Jordan Abraham
 */
insert<PlayerRenderingBlockListener>().fixedPlayerUpdateBlock<TemporaryMovementType>(index = 2, mask = 0x2000, size = 1) {
    it.writeByte(id)
}
