package script.block.npc

import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeShortLittleEndian
import xlitekt.game.actor.render.Render.FaceLocation
import xlitekt.game.actor.render.block.onNPCUpdateBlock
import xlitekt.shared.buffer.writeByteSubtract
import xlitekt.shared.buffer.writeShortLittleEndianAdd

/**
 * @author Jordan Abraham
 */
onNPCUpdateBlock<FaceLocation>(1, 0x8) {
    buildPacket {
        writeShortLittleEndian(((location.x shl 1) + 1).toShort())
        writeShortLittleEndianAdd(((location.z shl 1) + 1).toShort())
        writeByteSubtract(0) // 1 == instant look = 0 is delayed look
    }
}
