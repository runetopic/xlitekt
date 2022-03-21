package script.block.npc

import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeShort
import xlitekt.game.actor.render.Render.ForceMovement
import xlitekt.game.actor.render.block.onNPCUpdateBlock
import xlitekt.shared.buffer.writeByteNegate
import xlitekt.shared.buffer.writeShortLittleEndianAdd

/**
 * @author Jordan Abraham
 */
onNPCUpdateBlock<ForceMovement>(7, 0x400) {
    buildPacket {
        writeByteNegate((firstLocation.x - currentLocation.x).toByte())
        writeByte((firstLocation.z - currentLocation.z).toByte())
        writeByteNegate((secondLocation?.x?.minus(currentLocation.x) ?: 0).toByte())
        writeByte((secondLocation?.z?.minus(currentLocation.z) ?: 0).toByte())
        writeShortLittleEndianAdd((firstDelay * 30).toShort())
        writeShort((secondDelay * 30).toShort())
        writeShortLittleEndianAdd(rotation.toShort())
    }
}
