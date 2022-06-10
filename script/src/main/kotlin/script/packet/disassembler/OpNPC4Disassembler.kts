package script.packet.disassembler

import xlitekt.game.packet.OpNPCPacket
import xlitekt.game.packet.disassembler.onPacketDisassembler
import xlitekt.shared.buffer.readUByteAdd
import xlitekt.shared.buffer.readUShortLittleEndian
import xlitekt.shared.toBoolean

/**
 * @author Justin Kenney
 */
onPacketDisassembler(opcode = 74, size = 3) {
    OpNPCPacket(
        index = 4,
        npcIndex = readUShortLittleEndian(),
        isModified = readUByteAdd().toBoolean()
    )
}
