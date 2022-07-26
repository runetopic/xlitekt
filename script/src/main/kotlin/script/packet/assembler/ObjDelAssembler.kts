package script.packet.assembler

import xlitekt.game.packet.ObjDelPacket
import xlitekt.game.packet.assembler.PacketAssemblerListener
import xlitekt.shared.buffer.writeByteAdd
import xlitekt.shared.buffer.writeShortLittleEndian
import xlitekt.shared.insert

/**
 * @author Jordan Abraham
 */
insert<PacketAssemblerListener>().assemblePacket<ObjDelPacket>(opcode = 26, size = 3) {
    it.writeByteAdd(packedOffset)
    it.writeShortLittleEndian(id)
}
