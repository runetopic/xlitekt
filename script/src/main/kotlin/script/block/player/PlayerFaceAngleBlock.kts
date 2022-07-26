package script.block.player

import xlitekt.game.actor.render.Render.FaceAngle
import xlitekt.game.actor.render.block.PlayerRenderingBlockListener
import xlitekt.shared.buffer.writeShortLittleEndian
import xlitekt.shared.insert

/**
 * @author Jordan Abraham
 */
insert<PlayerRenderingBlockListener>().fixedPlayerUpdateBlock<FaceAngle>(index = 12, mask = 0x40, size = 2) {
    it.writeShortLittleEndian(angle)
}
