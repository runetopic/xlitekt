package script.block.npc

import xlitekt.game.actor.render.Render.Sequence
import xlitekt.game.actor.render.block.onNPCUpdateBlock
import xlitekt.shared.buffer.allocate
import xlitekt.shared.buffer.writeByteSubtract
import xlitekt.shared.buffer.writeShortLittleEndianAdd

/**
 * @author Jordan Abraham
 */
onNPCUpdateBlock<Sequence>(6, 0x40) {
    allocate(3) {
        writeShortLittleEndianAdd(id)
        writeByteSubtract(delay)
    }
}
