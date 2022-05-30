package script.packet.disassembler

import xlitekt.game.packet.ExamineItemPacket
import xlitekt.game.packet.disassembler.onPacketDisassembler
import xlitekt.shared.buffer.readUShortLittleEndian

/**
 * @author
 */
onPacketDisassembler(opcode = 100, size = 2) {
    ExamineItemPacket(
        itemID = readUShortLittleEndian()
    )
}
