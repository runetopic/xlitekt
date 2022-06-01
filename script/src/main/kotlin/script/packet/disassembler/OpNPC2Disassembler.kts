package script.packet.disassembler

import xlitekt.game.packet.OpNPCPacket
import xlitekt.game.packet.disassembler.onPacketDisassembler
import xlitekt.shared.buffer.readUByteSubtract
import xlitekt.shared.buffer.readUShortLittleEndianAdd
import xlitekt.shared.toBoolean

/**
 * @author Justin Kenney
 */
onPacketDisassembler(opcode = 59, size = 3) {
    OpNPCPacket(
        index = 2,
        npcIndex = readUShortLittleEndianAdd(),
        isModifiedClick = readUByteSubtract().toBoolean()
    )
}
