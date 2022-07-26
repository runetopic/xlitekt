package script.block.npc

import xlitekt.game.actor.render.Render.FaceActor
import xlitekt.game.actor.render.block.NPCRenderingBlockListener
import xlitekt.shared.buffer.writeShortLittleEndian
import xlitekt.shared.insert

/**
 * @author Jordan Abraham
 */
insert<NPCRenderingBlockListener>().fixedNpcUpdateBlock<FaceActor>(index = 8, mask = 0x80, size = 2) {
    it.writeShortLittleEndian(index)
}
