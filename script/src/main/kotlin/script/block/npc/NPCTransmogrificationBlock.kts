package script.block.npc

import xlitekt.game.actor.render.Render.NPCTransmogrification
import xlitekt.game.actor.render.block.onNPCUpdateBlock
import xlitekt.shared.buffer.buildFixedPacket
import xlitekt.shared.buffer.writeShortLittleEndian

/**
 * @author Jordan Abraham
 */
onNPCUpdateBlock<NPCTransmogrification>(0, 0x20) {
    buildFixedPacket(2) {
        writeShortLittleEndian(id)
    }
}
