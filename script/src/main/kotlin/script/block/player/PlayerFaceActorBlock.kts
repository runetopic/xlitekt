package script.block.player

import xlitekt.game.actor.render.Render.FaceActor
import xlitekt.game.actor.render.block.PlayerRenderingBlockListener
import xlitekt.shared.buffer.writeShortLittleEndianAdd
import xlitekt.shared.insert

/**
 * @author Jordan Abraham
 */
insert<PlayerRenderingBlockListener>().fixedPlayerUpdateBlock<FaceActor>(index = 4, mask = 0x80, size = 2) {
    it.writeShortLittleEndianAdd(index)
}
