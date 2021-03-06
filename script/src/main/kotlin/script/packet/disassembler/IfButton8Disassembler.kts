package script.packet.disassembler

import xlitekt.game.packet.IfButtonPacket
import xlitekt.game.packet.disassembler.PacketDisassemblerListener
import xlitekt.shared.buffer.readUShort
import xlitekt.shared.insert

/**
 * @author Jordan Abraham
 */
insert<PacketDisassemblerListener>().disassemblePacket(opcode = 82, size = 8) {
    IfButtonPacket(
        index = 8,
        packedInterface = readInt(),
        slotId = readUShort(),
        itemId = readUShort()
    )
}
