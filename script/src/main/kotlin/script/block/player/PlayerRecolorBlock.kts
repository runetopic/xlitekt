package script.block.player

import xlitekt.game.actor.render.Render.Recolor
import xlitekt.game.actor.render.block.onPlayerUpdateBlock
import xlitekt.shared.buffer.buildFixedPacket
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.buffer.writeByteSubtract
import xlitekt.shared.buffer.writeShortAdd
import xlitekt.shared.buffer.writeShortLittleEndianAdd

/**
 * @author Jordan Abraham
 */
onPlayerUpdateBlock<Recolor>(11, 0x200) {
    buildFixedPacket(8) {
        writeShortLittleEndianAdd(startDelay)
        writeShortAdd(endDelay)
        writeByte(hue)
        writeByteSubtract(saturation)
        writeByte(luminance)
        writeByte(amount)
    }
}
