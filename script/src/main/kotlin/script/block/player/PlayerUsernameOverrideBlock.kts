package script.block.player

import xlitekt.game.actor.render.Render.UsernameOverride
import xlitekt.game.actor.render.block.PlayerRenderingBlockListener
import xlitekt.shared.buffer.writeStringCp1252NullTerminated
import xlitekt.shared.insert

/**
 * @author Jordan Abraham
 */
insert<PlayerRenderingBlockListener>().dynamicPlayerUpdateBlock<UsernameOverride>(index = 6, mask = 0x100, size = -1) {
    it.writeStringCp1252NullTerminated(prefix)
    it.writeStringCp1252NullTerminated(infix)
    it.writeStringCp1252NullTerminated(suffix)
}
