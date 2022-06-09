package script.block.npc

import xlitekt.game.actor.render.Render.NPCTransmogrification
import xlitekt.game.actor.render.block.onNPCUpdateBlock
import xlitekt.shared.buffer.allocate
import xlitekt.shared.buffer.writeShortLittleEndian

/**
 * @author Jordan Abraham
 */
onNPCUpdateBlock<NPCTransmogrification>(0, 0x20) {
    allocate(2) {
        writeShortLittleEndian { id }
    }
}
