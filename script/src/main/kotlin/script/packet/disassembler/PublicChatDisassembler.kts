package script.packet.disassembler

import xlitekt.game.packet.PublicChatPacket
import xlitekt.game.packet.disassembler.onPacketDisassembler
import xlitekt.shared.buffer.readUByte
import xlitekt.shared.buffer.readUShortSmart

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
onPacketDisassembler(opcode = 95, size = -1) {
    PublicChatPacket(
        unknown = readUByte().toInt(),
        color = readUByte().toInt(),
        effect = readUByte().toInt(),
        length = readUShortSmart(),
        data = array().copyOfRange(position(), array().size)
    )
}
