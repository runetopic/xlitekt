package script.block.player

import xlitekt.game.actor.render.Render.FaceActor
import xlitekt.game.actor.render.block.fixedPlayerUpdateBlock
import xlitekt.shared.buffer.writeShortLittleEndianAdd

/**
 * @author Jordan Abraham
 */
fixedPlayerUpdateBlock<FaceActor>(index = 4, mask = 0x80, size = 2) {
    it.writeShortLittleEndianAdd(index)
}
