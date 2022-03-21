package script.block.player

import io.ktor.utils.io.core.buildPacket
import xlitekt.game.actor.render.Render.Recolor
import xlitekt.game.actor.render.block.onPlayerUpdateBlock
import xlitekt.shared.buffer.writeByteSubtract
import xlitekt.shared.buffer.writeShortAdd
import xlitekt.shared.buffer.writeShortLittleEndianAdd

/**
 * @author Jordan Abraham
 */
onPlayerUpdateBlock<Recolor>(11, 0x200) {
    buildPacket {
        writeShortLittleEndianAdd(startDelay.toShort())
        writeShortAdd(endDelay.toShort())
        writeByte(hue.toByte())
        writeByteSubtract(saturation.toByte())
        writeByte(luminance.toByte())
        writeByte(amount.toByte())
    }
}
