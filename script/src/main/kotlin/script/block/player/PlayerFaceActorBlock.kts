package script.block.player

import xlitekt.game.actor.render.Render.FaceActor
import xlitekt.game.actor.render.block.onPlayerUpdateBlock
import xlitekt.shared.buffer.allocate
import xlitekt.shared.buffer.writeShortLittleEndianAdd

/**
 * @author Jordan Abraham
 */
onPlayerUpdateBlock<FaceActor>(4, 0x80) {
    allocate(2) {
        writeShortLittleEndianAdd(index)
    }
}
