package script.block.player

import xlitekt.game.actor.render.Render.SpotAnimation
import xlitekt.game.actor.render.block.PlayerRenderingBlockListener
import xlitekt.shared.buffer.writeIntV2
import xlitekt.shared.buffer.writeShortAdd
import xlitekt.shared.insert

/**
 * @author Jordan Abraham
 */
insert<PlayerRenderingBlockListener>().fixedPlayerUpdateBlock<SpotAnimation>(index = 10, mask = 0x800, size = 6) {
    it.writeShortAdd(id)
    it.writeIntV2(packedMetaData())
}
