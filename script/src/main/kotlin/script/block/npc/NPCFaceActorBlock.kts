package script.block.npc

import xlitekt.game.actor.render.Render.FaceActor
import xlitekt.game.actor.render.block.fixedNpcUpdateBlock
import xlitekt.shared.buffer.writeShortLittleEndian

/**
 * @author Jordan Abraham
 */
fixedNpcUpdateBlock<FaceActor>(index = 8, mask = 0x80, size = 2) {
    it.writeShortLittleEndian(index)
}
