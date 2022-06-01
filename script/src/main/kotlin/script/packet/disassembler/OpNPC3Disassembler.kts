package script.packet.disassembler

import io.ktor.utils.io.core.readShort
import xlitekt.game.packet.OpNPCPacket
import xlitekt.game.packet.disassembler.onPacketDisassembler
import xlitekt.shared.toBoolean

/**
 * @author Justin Kenney
 */
onPacketDisassembler(opcode = 70, size = 3) {
    OpNPCPacket(
        index = 2,
        npcIndex = readShort().toInt(),
        isModifiedClick = readByte().toInt().toBoolean()
    )
}
