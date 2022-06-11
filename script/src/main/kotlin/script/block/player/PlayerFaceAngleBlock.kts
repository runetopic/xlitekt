package script.block.player

import xlitekt.game.actor.render.Render.FaceAngle
import xlitekt.game.actor.render.block.fixedPlayerUpdateBlock
import xlitekt.shared.buffer.writeShortLittleEndian

/**
 * @author Jordan Abraham
 */
fixedPlayerUpdateBlock<FaceAngle>(index = 12, mask = 0x40, size = 2) {
    it.writeShortLittleEndian(angle)
}
