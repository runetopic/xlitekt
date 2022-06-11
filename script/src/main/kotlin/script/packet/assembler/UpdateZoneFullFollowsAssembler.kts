package script.packet.assembler

import xlitekt.game.packet.UpdateZoneFullFollowsPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.buffer.writeByteAdd

/**
 * @author Jordan Abraham
 */
onPacketAssembler<UpdateZoneFullFollowsPacket>(opcode = 16, size = 2) {
    it.writeByteAdd((localX shr 3) shl 3)
    it.writeByte((localZ shr 3) shl 3)
}
