package script.block.player

import xlitekt.game.actor.render.Render.ForceMovement
import xlitekt.game.actor.render.block.fixedPlayerUpdateBlock
import xlitekt.shared.buffer.writeByteNegate
import xlitekt.shared.buffer.writeByteSubtract
import xlitekt.shared.buffer.writeShortAdd
import xlitekt.shared.buffer.writeShortLittleEndianAdd

/**
 * @author Jordan Abraham
 */
fixedPlayerUpdateBlock<ForceMovement>(index = 3, mask = 0x4000, size = 10) {
    it.writeByteNegate(firstLocation.x - currentLocation.x)
    it.writeByteNegate(firstLocation.z - currentLocation.z)
    it.writeByteSubtract(secondLocation?.x?.minus(currentLocation.x) ?: 0)
    it.writeByteSubtract(secondLocation?.z?.minus(currentLocation.z) ?: 0)
    it.writeShortLittleEndianAdd(firstDelay * 30)
    it.writeShortLittleEndianAdd(secondDelay * 30)
    it.writeShortAdd(rotation)
}
