package script.packet.assembler

import xlitekt.game.packet.UpdateZonePartialEnclosedPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.buffer.writeByteNegate
import xlitekt.shared.buffer.writeBytes

/**
 * @author Jordan Abraham
 */
onPacketAssembler<UpdateZonePartialEnclosedPacket>(opcode = 56, size = -2) {
    it.writeByteNegate((localZ shr 3) shl 3)
    it.writeByte((localX shr 3) shl 3)
    it.writeBytes(bytes)
}
