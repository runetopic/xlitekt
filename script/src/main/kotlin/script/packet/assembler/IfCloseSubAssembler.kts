package script.packet.assembler

import xlitekt.game.packet.IfCloseSubPacket
import xlitekt.game.packet.assembler.PacketAssemblerListener
import xlitekt.shared.buffer.writeInt
import xlitekt.shared.insert

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
insert<PacketAssemblerListener>().assemblePacket<IfCloseSubPacket>(opcode = 13, size = 4) {
    it.writeInt(packedInterface)
}
