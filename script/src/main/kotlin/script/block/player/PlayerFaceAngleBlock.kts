package script.block.player

import xlitekt.game.actor.render.Render.FaceAngle
import xlitekt.game.actor.render.block.onPlayerUpdateBlock
import xlitekt.shared.buffer.buildFixedPacket
import xlitekt.shared.buffer.writeShortLittleEndian

/**
 * @author Jordan Abraham
 */
onPlayerUpdateBlock<FaceAngle>(12, 0x40) {
    buildFixedPacket(2) {
        writeShortLittleEndian(angle)
    }
}
