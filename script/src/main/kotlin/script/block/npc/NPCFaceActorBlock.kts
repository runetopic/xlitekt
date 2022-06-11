package script.block.npc

import xlitekt.game.actor.render.Render.FaceActor
import xlitekt.game.actor.render.block.onNPCUpdateBlock
import xlitekt.shared.buffer.buildFixedPacket
import xlitekt.shared.buffer.writeShortLittleEndian

/**
 * @author Jordan Abraham
 */
onNPCUpdateBlock<FaceActor>(8, 0x80) {
    buildFixedPacket(2) {
        writeShortLittleEndian(index)
    }
}
