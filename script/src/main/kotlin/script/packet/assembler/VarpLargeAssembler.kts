package script.packet.assembler

import xlitekt.game.packet.VarpLargePacket
import xlitekt.game.packet.assembler.PacketAssemblerListener
import xlitekt.shared.buffer.writeIntV1
import xlitekt.shared.buffer.writeShortLittleEndianAdd
import xlitekt.shared.insert

/**
 * @author Jordan Abraham
 */
insert<PacketAssemblerListener>().assemblePacket<VarpLargePacket>(opcode = 0, size = 6) {
    it.writeIntV1(value)
    it.writeShortLittleEndianAdd(id)
}
