package script.packet.disassembler

import xlitekt.game.packet.ClientCheatPacket
import xlitekt.game.packet.disassembler.onPacketDisassembler
import xlitekt.shared.buffer.readStringCp1252NullTerminated

/**
 * @author Jordan Abraham
 */
onPacketDisassembler(opcode = 44, size = -1) {
    ClientCheatPacket(
        command = readStringCp1252NullTerminated()
    )
}
