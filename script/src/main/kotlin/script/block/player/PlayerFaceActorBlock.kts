package script.block.player

import xlitekt.game.actor.render.Render.FaceActor
import xlitekt.game.actor.render.block.onPlayerUpdateBlock
import xlitekt.shared.buffer.buildFixedPacket
import xlitekt.shared.buffer.writeShortLittleEndianAdd

/**
 * @author Jordan Abraham
 */
onPlayerUpdateBlock<FaceActor>(4, 0x80) {
    buildFixedPacket(2) {
        writeShortLittleEndianAdd(index)
    }
}
