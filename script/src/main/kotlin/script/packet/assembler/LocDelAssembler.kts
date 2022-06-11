package script.packet.assembler

import xlitekt.game.packet.LocDelPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.buffer.writeByteAdd

/**
 * @author Jordan Abraham
 */
onPacketAssembler<LocDelPacket>(opcode = 66, size = 2) {
    it.writeByte((shape shl 2) or (rotation and 0x3))
    it.writeByteAdd(packedOffset)
}
