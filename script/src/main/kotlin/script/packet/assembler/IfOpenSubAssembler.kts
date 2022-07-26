package script.packet.assembler

import xlitekt.game.packet.IfOpenSubPacket
import xlitekt.game.packet.assembler.PacketAssemblerListener
import xlitekt.shared.buffer.writeByteSubtract
import xlitekt.shared.buffer.writeInt
import xlitekt.shared.buffer.writeShortLittleEndian
import xlitekt.shared.insert
import xlitekt.shared.toInt

/**
 * @author Jordan Abraham
 */
insert<PacketAssemblerListener>().assemblePacket<IfOpenSubPacket>(opcode = 2, size = 7) {
    it.writeShortLittleEndian(interfaceId)
    it.writeByteSubtract(walkable.toInt())
    it.writeInt(toPackedInterface)
}
