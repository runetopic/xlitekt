package script.packet.disassembler

import io.ktor.utils.io.core.readShort
import xlitekt.game.packet.OpNPCPacket
import xlitekt.game.packet.disassembler.onPacketDisassembler
import xlitekt.shared.buffer.readByte
import xlitekt.shared.buffer.readShort
import xlitekt.shared.toBoolean

/**
 * @author Justin Kenney
 */
onPacketDisassembler(opcode = 70, size = 3) {
    OpNPCPacket(
        index = 3,
        npcIndex = readShort(),
        isModified = readByte().toBoolean()
    )
}
