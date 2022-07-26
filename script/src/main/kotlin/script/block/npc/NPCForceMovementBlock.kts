package script.block.npc

import xlitekt.game.actor.render.Render.ForceMovement
import xlitekt.game.actor.render.block.NPCRenderingBlockListener
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.buffer.writeByteNegate
import xlitekt.shared.buffer.writeShort
import xlitekt.shared.buffer.writeShortLittleEndianAdd
import xlitekt.shared.insert

/**
 * @author Jordan Abraham
 */
insert<NPCRenderingBlockListener>().fixedNpcUpdateBlock<ForceMovement>(index = 7, mask = 0x400, size = 10) {
    it.writeByteNegate(firstLocation.x - currentLocation.x)
    it.writeByte(firstLocation.z - currentLocation.z)
    it.writeByteNegate(secondLocation?.x?.minus(currentLocation.x) ?: 0)
    it.writeByte(secondLocation?.z?.minus(currentLocation.z) ?: 0)
    it.writeShortLittleEndianAdd(firstDelay * 30)
    it.writeShort(secondDelay * 30)
    it.writeShortLittleEndianAdd(rotation)
}
