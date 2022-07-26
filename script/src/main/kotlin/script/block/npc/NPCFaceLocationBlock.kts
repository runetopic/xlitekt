package script.block.npc

import xlitekt.game.actor.render.Render.FaceLocation
import xlitekt.game.actor.render.block.NPCRenderingBlockListener
import xlitekt.shared.buffer.writeByteSubtract
import xlitekt.shared.buffer.writeShortLittleEndian
import xlitekt.shared.buffer.writeShortLittleEndianAdd
import xlitekt.shared.insert

/**
 * @author Jordan Abraham
 */
insert<NPCRenderingBlockListener>().fixedNpcUpdateBlock<FaceLocation>(index = 1, mask = 0x8, size = 5) {
    it.writeShortLittleEndian((location.x shl 1) + 1)
    it.writeShortLittleEndianAdd((location.z shl 1) + 1)
    it.writeByteSubtract(0) // 1 == instant look = 0 is delayed look
}
