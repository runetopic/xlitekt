package script.block.npc

import xlitekt.game.actor.render.Render.Recolor
import xlitekt.game.actor.render.block.onNPCUpdateBlock
import xlitekt.shared.buffer.allocate
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.buffer.writeByteNegate
import xlitekt.shared.buffer.writeShort
import xlitekt.shared.buffer.writeShortLittleEndian

/**
 * @author Jordan Abraham
 */
onNPCUpdateBlock<Recolor>(9, 0x100) {
    allocate(8) {
        writeShort { startDelay }
        writeShortLittleEndian { endDelay }
        writeByte { hue }
        writeByteNegate { saturation }
        writeByteNegate { luminance }
        writeByte { amount }
    }
}
