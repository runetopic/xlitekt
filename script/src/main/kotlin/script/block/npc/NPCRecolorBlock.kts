package script.block.npc

import xlitekt.game.actor.render.Render.Recolor
import xlitekt.game.actor.render.block.NPCRenderingBlockListener
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.buffer.writeByteNegate
import xlitekt.shared.buffer.writeShort
import xlitekt.shared.buffer.writeShortLittleEndian
import xlitekt.shared.insert

/**
 * @author Jordan Abraham
 */
insert<NPCRenderingBlockListener>().fixedNpcUpdateBlock<Recolor>(index = 9, mask = 0x100, size = 8) {
    it.writeShort(startDelay)
    it.writeShortLittleEndian(endDelay)
    it.writeByte(hue)
    it.writeByteNegate(saturation)
    it.writeByteNegate(luminance)
    it.writeByte(amount)
}
