package script.packet.disassembler

import xlitekt.game.packet.ExamineItemPacket
import xlitekt.game.packet.disassembler.PacketDisassemblerListener
import xlitekt.shared.buffer.readUShortLittleEndian
import xlitekt.shared.insert

/**
 * @author Justin Kenney
 */
insert<PacketDisassemblerListener>().disassemblePacket(opcode = 100, size = 2) {
    ExamineItemPacket(
        itemId = readUShortLittleEndian()
    )
}
