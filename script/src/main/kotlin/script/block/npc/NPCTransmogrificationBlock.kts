package script.block.npc

import xlitekt.game.actor.render.Render.NPCTransmogrification
import xlitekt.game.actor.render.block.NPCRenderingBlockListener
import xlitekt.shared.buffer.writeShortLittleEndian
import xlitekt.shared.insert

/**
 * @author Jordan Abraham
 */
insert<NPCRenderingBlockListener>().fixedNpcUpdateBlock<NPCTransmogrification>(index = 0, mask = 0x20, size = 2) {
    it.writeShortLittleEndian(id)
}
