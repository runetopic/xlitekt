package script.block.player

import xlitekt.game.actor.render.Render.ForceMovement
import xlitekt.game.actor.render.block.PlayerRenderingBlockListener
import xlitekt.shared.buffer.writeByteNegate
import xlitekt.shared.buffer.writeByteSubtract
import xlitekt.shared.buffer.writeShortAdd
import xlitekt.shared.buffer.writeShortLittleEndianAdd
import xlitekt.shared.insert

/**
 * @author Jordan Abraham
 */
insert<PlayerRenderingBlockListener>().fixedPlayerUpdateBlock<ForceMovement>(index = 3, mask = 0x4000, size = 10) {
    it.writeByteNegate(firstLocation.x - currentLocation.x)
    it.writeByteNegate(firstLocation.z - currentLocation.z)
    it.writeByteSubtract(secondLocation?.x?.minus(currentLocation.x) ?: 0)
    it.writeByteSubtract(secondLocation?.z?.minus(currentLocation.z) ?: 0)
    it.writeShortLittleEndianAdd(firstDelay * 30)
    it.writeShortLittleEndianAdd(secondDelay * 30)
    it.writeShortAdd(rotation)
}
