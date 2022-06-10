package script.block.player

import xlitekt.game.actor.render.Render.FaceAngle
import xlitekt.game.actor.render.block.onPlayerUpdateBlock
import xlitekt.shared.buffer.allocate
import xlitekt.shared.buffer.writeShortLittleEndian

/**
 * @author Jordan Abraham
 */
onPlayerUpdateBlock<FaceAngle>(12, 0x40) {
    allocate(2) {
        writeShortLittleEndian(angle)
    }
}
