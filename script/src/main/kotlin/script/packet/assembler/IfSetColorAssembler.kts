package script.packet.assembler

import xlitekt.game.packet.IfSetColorPacket
import xlitekt.game.packet.assembler.PacketAssemblerListener
import xlitekt.shared.buffer.writeIntV2
import xlitekt.shared.buffer.writeShortLittleEndian
import xlitekt.shared.insert

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
insert<PacketAssemblerListener>().assemblePacket<IfSetColorPacket>(opcode = 62, size = 6) {
    it.writeIntV2(packedInterface)
    it.writeShortLittleEndian(color)
}
