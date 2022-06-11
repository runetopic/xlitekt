package script.block.npc

import xlitekt.game.actor.render.Render.OverheadChat
import xlitekt.game.actor.render.block.dynamicNpcUpdateBlock
import xlitekt.shared.buffer.writeStringCp1252NullTerminated

/**
 * @author Jordan Abraham
 */
dynamicNpcUpdateBlock<OverheadChat>(index = 3, mask = 0x10, size = -1) {
    it.writeStringCp1252NullTerminated(text)
}
