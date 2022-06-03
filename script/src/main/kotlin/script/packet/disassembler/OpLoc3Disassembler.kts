package script.packet.disassembler

import io.ktor.utils.io.core.readShort
import xlitekt.game.packet.OpLocPacket
import xlitekt.game.packet.disassembler.onPacketDisassembler
import xlitekt.shared.buffer.readUByteSubtract
import xlitekt.shared.buffer.readUShortLittleEndianAdd
import xlitekt.shared.toBoolean

/**
 * @author Justin Kenney
 */
onPacketDisassembler(opcode = 54, size = 7) {
    OpLocPacket(
        index = 3,
        objectId = readUShortLittleEndianAdd(),
        z = readUShortLittleEndianAdd(),
        x = readShort().toInt(),
        isModified = readUByteSubtract().toBoolean()
    )
}
