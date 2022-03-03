package script.packet.disassembler

import io.ktor.utils.io.core.readUShort
import xlitekt.game.packet.WindowStatusPacket
import xlitekt.game.packet.disassembler.onPacketDisassembler

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
onPacketDisassembler(opcode = 48, size = 5) {
    WindowStatusPacket(
        displayMode = readByte().toInt(),
        width = readUShort().toInt(),
        height = readUShort().toInt()
    )
}
