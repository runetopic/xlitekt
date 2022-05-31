package script.packet.disassembler

import xlitekt.game.packet.ExamineNPCPacket
import xlitekt.game.packet.disassembler.onPacketDisassembler
import xlitekt.shared.buffer.readUShortLittleEndian

/**
 * @author Justin Kenney
 */
onPacketDisassembler(opcode = 22, size = 2) {
    ExamineNPCPacket(
        npcID = readUShortLittleEndian()
    )
}
