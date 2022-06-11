package script.block.npc

import xlitekt.game.actor.render.Render.NPCCustomLevel
import xlitekt.game.actor.render.block.fixedNpcUpdateBlock
import xlitekt.shared.buffer.writeIntV1

/**
 * @author Jordan Abraham
 */
fixedNpcUpdateBlock<NPCCustomLevel>(index = 5, mask = 0x200, size = 4) {
    it.writeIntV1(level)
}
