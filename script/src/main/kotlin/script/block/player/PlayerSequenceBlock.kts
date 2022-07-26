package script.block.player

import xlitekt.game.actor.render.Render.Sequence
import xlitekt.game.actor.render.block.PlayerRenderingBlockListener
import xlitekt.shared.buffer.writeByteNegate
import xlitekt.shared.buffer.writeShortAdd
import xlitekt.shared.insert

/**
 * @author Jordan Abraham
 */
insert<PlayerRenderingBlockListener>().fixedPlayerUpdateBlock<Sequence>(index = 8, mask = 0x2, size = 3) {
    it.writeShortAdd(id)
    it.writeByteNegate(delay)
}
