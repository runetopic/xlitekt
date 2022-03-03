package script.packet.disassembler

import io.ktor.utils.io.core.readBytes
import io.ktor.utils.io.core.readUByte
import xlitekt.game.packet.PublicChatPacket
import xlitekt.game.packet.disassembler.onPacketDisassembler
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
        data = readBytes()
    )
}
