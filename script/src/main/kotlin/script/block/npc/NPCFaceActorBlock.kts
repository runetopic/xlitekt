package script.block.npc

import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeShortLittleEndian
import xlitekt.game.actor.render.Render.FaceActor
import xlitekt.game.actor.render.block.onNPCUpdateBlock
import xlitekt.shared.buffer.writeShortLittleEndian

/**
 * @author Jordan Abraham
 */
onNPCUpdateBlock<FaceActor>(8, 0x80) {
    buildPacket {
        writeShortLittleEndian { index }
    }
}
