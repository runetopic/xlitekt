package script.block.npc

import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeShort
import io.ktor.utils.io.core.writeShortLittleEndian
import xlitekt.game.actor.render.Render.Recolor
import xlitekt.game.actor.render.block.onNPCUpdateBlock
import xlitekt.shared.buffer.writeByteNegate

/**
 * @author Jordan Abraham
 */
onNPCUpdateBlock<Recolor>(9, 0x100) {
    buildPacket {
        writeShort(startDelay.toShort())
        writeShortLittleEndian(endDelay.toShort())
        writeByte(hue.toByte())
        writeByteNegate(saturation.toByte())
        writeByteNegate(luminance.toByte())
        writeByte(amount.toByte())
    }
}
