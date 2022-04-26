package script.block.npc

import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeShortLittleEndian
import xlitekt.game.actor.render.Render.NPCTransmogrification
import xlitekt.game.actor.render.block.onNPCUpdateBlock
import xlitekt.shared.buffer.writeShortLittleEndian

/**
 * @author Jordan Abraham
 */
onNPCUpdateBlock<NPCTransmogrification>(0, 0x20) {
    buildPacket {
        writeShortLittleEndian { id }
    }
}
