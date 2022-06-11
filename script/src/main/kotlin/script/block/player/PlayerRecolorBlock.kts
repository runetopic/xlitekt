package script.block.player

import xlitekt.game.actor.render.Render.Recolor
import xlitekt.game.actor.render.block.fixedPlayerUpdateBlock
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.buffer.writeByteSubtract
import xlitekt.shared.buffer.writeShortAdd
import xlitekt.shared.buffer.writeShortLittleEndianAdd

/**
 * @author Jordan Abraham
 */
fixedPlayerUpdateBlock<Recolor>(index = 11, mask = 0x200, size = 8) {
    it.writeShortLittleEndianAdd(startDelay)
    it.writeShortAdd(endDelay)
    it.writeByte(hue)
    it.writeByteSubtract(saturation)
    it.writeByte(luminance)
    it.writeByte(amount)
}
