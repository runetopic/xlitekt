package script.packet.disassembler

import io.ktor.utils.io.core.readBytes
import xlitekt.game.packet.PublicChatPacket
import xlitekt.game.packet.disassembler.PacketDisassemblerListener
import xlitekt.shared.buffer.readUByte
import xlitekt.shared.buffer.readUShortSmart
import xlitekt.shared.insert

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
insert<PacketDisassemblerListener>().disassemblePacket(opcode = 95, size = -1) {
    val mark = availableForRead
    PublicChatPacket(
        unknown = readUByte(),
        color = readUByte(),
        effect = readUByte(),
        length = readUShortSmart(),
        data = readPacket(it - (mark - availableForRead)).readBytes()
    )
}
