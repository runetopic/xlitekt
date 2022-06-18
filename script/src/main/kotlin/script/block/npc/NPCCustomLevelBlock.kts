package script.block.npc

import xlitekt.game.actor.render.Render.NPCCustomLevel
import xlitekt.game.actor.render.block.NPCRenderingBlockListener
import xlitekt.shared.buffer.writeIntV1
import xlitekt.shared.insert

/**
 * @author Jordan Abraham
 */
insert<NPCRenderingBlockListener>().fixedNpcUpdateBlock<NPCCustomLevel>(index = 5, mask = 0x200, size = 4) {
    it.writeIntV1(level)
}
