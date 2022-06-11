package script.block.player

import xlitekt.game.actor.render.Render.Sequence
import xlitekt.game.actor.render.block.fixedPlayerUpdateBlock
import xlitekt.shared.buffer.writeByteNegate
import xlitekt.shared.buffer.writeShortAdd

/**
 * @author Jordan Abraham
 */
fixedPlayerUpdateBlock<Sequence>(index = 8, mask = 0x2, size = 3) {
    it.writeShortAdd(id)
    it.writeByteNegate(delay)
}
