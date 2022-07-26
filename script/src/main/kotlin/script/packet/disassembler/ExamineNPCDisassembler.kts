package script.packet.disassembler

import xlitekt.game.packet.ExamineNPCPacket
import xlitekt.game.packet.disassembler.PacketDisassemblerListener
import xlitekt.shared.buffer.readUShortLittleEndian
import xlitekt.shared.insert

/**
 * @author Justin Kenney
 */
insert<PacketDisassemblerListener>().disassemblePacket(opcode = 22, size = 2) {
    ExamineNPCPacket(
        npcId = readUShortLittleEndian()
    )
}
