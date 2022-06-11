package script.block.npc

import xlitekt.game.actor.render.Render.Sequence
import xlitekt.game.actor.render.block.fixedNpcUpdateBlock
import xlitekt.shared.buffer.writeByteSubtract
import xlitekt.shared.buffer.writeShortLittleEndianAdd

/**
 * @author Jordan Abraham
 */
fixedNpcUpdateBlock<Sequence>(index = 6, mask = 0x40, size = 3) {
    it.writeShortLittleEndianAdd(id)
    it.writeByteSubtract(delay)
}
