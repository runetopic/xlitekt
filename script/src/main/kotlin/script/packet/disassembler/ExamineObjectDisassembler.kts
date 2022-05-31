package script.packet.disassembler

import xlitekt.game.packet.ExamineObjectPacket
import xlitekt.game.packet.disassembler.onPacketDisassembler
import xlitekt.shared.buffer.readUShortAdd

/**
 * @author Justin Kenney
 */
onPacketDisassembler(opcode = 13, size = 2) {
    ExamineObjectPacket(
        objectID = readUShortAdd()
    )
}
