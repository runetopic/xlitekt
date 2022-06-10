package script.packet.disassembler

import xlitekt.game.packet.OpLocPacket
import xlitekt.game.packet.disassembler.onPacketDisassembler
import xlitekt.shared.buffer.readByte
import xlitekt.shared.buffer.readUShortAdd
import xlitekt.shared.buffer.readUShortLittleEndian
import xlitekt.shared.toBoolean

/**
 * @author Jordan Abraham
 */
onPacketDisassembler(opcode = 94, size = 7) {
    OpLocPacket(
        index = 2,
        z = readUShortLittleEndian(),
        isModified = readByte().toBoolean(),
        x = readUShortAdd(),
        locId = readUShortLittleEndian()
    )
}
