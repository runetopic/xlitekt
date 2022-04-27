package script.block.npc

import io.ktor.utils.io.core.buildPacket
import xlitekt.game.actor.render.Render.Recolor
import xlitekt.game.actor.render.block.onNPCUpdateBlock
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.buffer.writeByteNegate
import xlitekt.shared.buffer.writeShort
import xlitekt.shared.buffer.writeShortLittleEndian

/**
 * @author Jordan Abraham
 */
onNPCUpdateBlock<Recolor>(9, 0x100) {
    buildPacket {
        writeShort { startDelay }
        writeShortLittleEndian { endDelay }
        writeByte { hue }
        writeByteNegate { saturation }
        writeByteNegate { luminance }
        writeByte { amount }
    }
}
