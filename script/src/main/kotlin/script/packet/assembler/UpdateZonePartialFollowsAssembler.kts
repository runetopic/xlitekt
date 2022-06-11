package script.packet.assembler

import xlitekt.game.packet.UpdateZonePartialFollowsPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.buildFixedPacket
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.buffer.writeByteSubtract

/**
 * @author Jordan Abraham
 */
onPacketAssembler<UpdateZonePartialFollowsPacket>(opcode = 53, size = 2) {
    buildFixedPacket(2) {
        writeByte((localZ shr 3) shl 3)
        writeByteSubtract((localX shr 3) shl 3)
    }
}
