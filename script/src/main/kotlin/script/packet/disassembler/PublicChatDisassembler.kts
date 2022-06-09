package script.packet.disassembler

import io.ktor.util.moveToByteArray
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
        unknown = readUByte(),
        color = readUByte(),
        effect = readUByte(),
        length = readUShortSmart(),
        data = moveToByteArray()
    )
}
