package script.block.npc

import xlitekt.game.actor.render.Render.FaceLocation
import xlitekt.game.actor.render.block.onNPCUpdateBlock
import xlitekt.shared.buffer.buildFixedPacket
import xlitekt.shared.buffer.writeByteSubtract
import xlitekt.shared.buffer.writeShortLittleEndian
import xlitekt.shared.buffer.writeShortLittleEndianAdd

/**
 * @author Jordan Abraham
 */
onNPCUpdateBlock<FaceLocation>(1, 0x8) {
    buildFixedPacket(5) {
        writeShortLittleEndian((location.x shl 1) + 1)
        writeShortLittleEndianAdd((location.z shl 1) + 1)
        writeByteSubtract(0) // 1 == instant look = 0 is delayed look
    }
}
