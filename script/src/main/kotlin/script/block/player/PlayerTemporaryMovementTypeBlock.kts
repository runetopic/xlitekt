package script.block.player

import xlitekt.game.actor.render.Render.TemporaryMovementType
import xlitekt.game.actor.render.block.fixedPlayerUpdateBlock
import xlitekt.shared.buffer.writeByte

/**
 * @author Jordan Abraham
 */
fixedPlayerUpdateBlock<TemporaryMovementType>(index = 2, mask = 0x2000, size = 1) {
    it.writeByte(id)
}
