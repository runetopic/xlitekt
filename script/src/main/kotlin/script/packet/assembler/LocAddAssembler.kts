package script.packet.assembler

import xlitekt.game.packet.LocAddPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.writeByteSubtract
import xlitekt.shared.buffer.writeShortLittleEndianAdd

/**
 * @author Jordan Abraham
 */
onPacketAssembler<LocAddPacket>(opcode = 18, size = 4) {
    it.writeByteSubtract((shape shl 2) or (rotation and 0x3))
    it.writeByteSubtract(packedOffset)
    it.writeShortLittleEndianAdd(id)
}
