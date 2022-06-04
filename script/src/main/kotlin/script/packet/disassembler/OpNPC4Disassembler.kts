package script.packet.disassembler

import io.ktor.utils.io.core.*
import xlitekt.game.packet.OpNPCPacket
import xlitekt.game.packet.disassembler.onPacketDisassembler
import xlitekt.shared.buffer.readUByteAdd
import xlitekt.shared.toBoolean

/**
 * @author Justin Kenney
 */
onPacketDisassembler(opcode = 74, size = 3) {
    OpNPCPacket(
        index = 4,
        npcIndex = readShortLittleEndian().toInt(),
        isModified = readUByteAdd().toBoolean()
    )
}
