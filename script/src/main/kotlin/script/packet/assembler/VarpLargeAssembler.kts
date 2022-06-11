package script.packet.assembler

import xlitekt.game.packet.VarpLargePacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.writeIntV1
import xlitekt.shared.buffer.writeShortLittleEndianAdd

/**
 * @author Jordan Abraham
 */
onPacketAssembler<VarpLargePacket>(opcode = 0, size = 6) {
    it.writeIntV1(value)
    it.writeShortLittleEndianAdd(id)
}
