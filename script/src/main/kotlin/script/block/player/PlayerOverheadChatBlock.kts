package script.block.player

import xlitekt.game.actor.render.Render.OverheadChat
import xlitekt.game.actor.render.block.dynamicPlayerUpdateBlock
import xlitekt.shared.buffer.writeStringCp1252NullTerminated

/**
 * @author Jordan Abraham
 */
dynamicPlayerUpdateBlock<OverheadChat>(index = 13, mask = 0x8, size = -1) {
    it.writeStringCp1252NullTerminated(text)
}
