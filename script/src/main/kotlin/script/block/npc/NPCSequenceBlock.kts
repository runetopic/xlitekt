package script.block.npc

import xlitekt.game.actor.render.Render.Sequence
import xlitekt.game.actor.render.block.NPCRenderingBlockListener
import xlitekt.shared.buffer.writeByteSubtract
import xlitekt.shared.buffer.writeShortLittleEndianAdd
import xlitekt.shared.insert

/**
 * @author Jordan Abraham
 */
insert<NPCRenderingBlockListener>().fixedNpcUpdateBlock<Sequence>(index = 6, mask = 0x40, size = 3) {
    it.writeShortLittleEndianAdd(id)
    it.writeByteSubtract(delay)
}
