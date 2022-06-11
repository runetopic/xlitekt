package script.block.npc

import xlitekt.game.actor.render.Render.NPCTransmogrification
import xlitekt.game.actor.render.block.fixedNpcUpdateBlock
import xlitekt.shared.buffer.writeShortLittleEndian

/**
 * @author Jordan Abraham
 */
fixedNpcUpdateBlock<NPCTransmogrification>(index = 0, mask = 0x20, size = 2) {
    it.writeShortLittleEndian(id)
}
