package script.block.player

import xlitekt.game.actor.render.Render.Recolor
import xlitekt.game.actor.render.block.PlayerRenderingBlockListener
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.buffer.writeByteSubtract
import xlitekt.shared.buffer.writeShortAdd
import xlitekt.shared.buffer.writeShortLittleEndianAdd
import xlitekt.shared.insert

/**
 * @author Jordan Abraham
 */
insert<PlayerRenderingBlockListener>().fixedPlayerUpdateBlock<Recolor>(index = 11, mask = 0x200, size = 8) {
    it.writeShortLittleEndianAdd(startDelay)
    it.writeShortAdd(endDelay)
    it.writeByte(hue)
    it.writeByteSubtract(saturation)
    it.writeByte(luminance)
    it.writeByte(amount)
}
