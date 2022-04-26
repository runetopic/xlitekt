package script.block.npc

import io.ktor.utils.io.core.buildPacket
import xlitekt.game.actor.render.Render.Sequence
import xlitekt.game.actor.render.block.onNPCUpdateBlock
import xlitekt.shared.buffer.writeByteSubtract
import xlitekt.shared.buffer.writeShortLittleEndianAdd

/**
 * @author Jordan Abraham
 */
onNPCUpdateBlock<Sequence>(6, 0x40) {
    buildPacket {
        writeShortLittleEndianAdd { id }
        writeByteSubtract { delay }
    }
}
