package script.packet.assembler

import xlitekt.game.packet.VarpSmallPacket
import xlitekt.game.packet.assembler.PacketAssemblerListener
import xlitekt.shared.buffer.writeByteAdd
import xlitekt.shared.buffer.writeShort
import xlitekt.shared.insert

/**
 * @author Jordan Abraham
 */
insert<PacketAssemblerListener>().assemblePacket<VarpSmallPacket>(opcode = 94, size = 3) {
    it.writeByteAdd(value)
    it.writeShort(id)
}
