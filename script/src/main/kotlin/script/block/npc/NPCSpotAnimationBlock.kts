package script.block.npc

import xlitekt.game.actor.render.Render.SpotAnimation
import xlitekt.game.actor.render.block.NPCRenderingBlockListener
import xlitekt.shared.buffer.writeIntV2
import xlitekt.shared.buffer.writeShortLittleEndianAdd
import xlitekt.shared.insert

/**
 * @author Jordan Abraham
 */
insert<NPCRenderingBlockListener>().fixedNpcUpdateBlock<SpotAnimation>(index = 4, mask = 0x2, size = 6) {
    it.writeShortLittleEndianAdd(id)
    it.writeIntV2(packedMetaData())
}
