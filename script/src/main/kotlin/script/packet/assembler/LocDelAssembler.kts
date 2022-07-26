package script.packet.assembler

import xlitekt.game.packet.LocDelPacket
import xlitekt.game.packet.assembler.PacketAssemblerListener
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.buffer.writeByteAdd
import xlitekt.shared.insert

/**
 * @author Jordan Abraham
 */
insert<PacketAssemblerListener>().assemblePacket<LocDelPacket>(opcode = 66, size = 2) {
    it.writeByte((shape shl 2) or (rotation and 0x3))
    it.writeByteAdd(packedOffset)
}
