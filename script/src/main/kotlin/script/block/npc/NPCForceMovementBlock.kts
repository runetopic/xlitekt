package script.block.npc

import xlitekt.game.actor.render.Render.ForceMovement
import xlitekt.game.actor.render.block.onNPCUpdateBlock
import xlitekt.shared.buffer.allocate
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.buffer.writeByteNegate
import xlitekt.shared.buffer.writeShort
import xlitekt.shared.buffer.writeShortLittleEndianAdd

/**
 * @author Jordan Abraham
 */
onNPCUpdateBlock<ForceMovement>(7, 0x400) {
    allocate(10) {
        writeByteNegate(firstLocation.x - currentLocation.x)
        writeByte(firstLocation.z - currentLocation.z)
        writeByteNegate(secondLocation?.x?.minus(currentLocation.x) ?: 0)
        writeByte(secondLocation?.z?.minus(currentLocation.z) ?: 0)
        writeShortLittleEndianAdd(firstDelay * 30)
        writeShort(secondDelay * 30)
        writeShortLittleEndianAdd(rotation)
    }
}
