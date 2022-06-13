package script.packet.disassembler

import xlitekt.game.packet.WindowStatusPacket
import xlitekt.game.packet.disassembler.onPacketDisassembler
import xlitekt.shared.buffer.readByte
import xlitekt.shared.buffer.readUShort

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
onPacketDisassembler(opcode = 48, size = 5) {
    WindowStatusPacket(
        displayMode = readByte().toInt(),
        width = readUShort(),
        height = readUShort()
    )
}
