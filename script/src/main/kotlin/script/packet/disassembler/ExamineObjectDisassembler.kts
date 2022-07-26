package script.packet.disassembler

import xlitekt.game.packet.ExamineObjectPacket
import xlitekt.game.packet.disassembler.PacketDisassemblerListener
import xlitekt.shared.buffer.readUShortAdd
import xlitekt.shared.insert

/**
 * @author Justin Kenney
 */
insert<PacketDisassemblerListener>().disassemblePacket(opcode = 13, size = 2) {
    ExamineObjectPacket(
        objectID = readUShortAdd()
    )
}
