package script.packet.disassembler

import xlitekt.game.packet.ClientCheatPacket
import xlitekt.game.packet.disassembler.PacketDisassemblerListener
import xlitekt.shared.buffer.readStringCp1252NullTerminated
import xlitekt.shared.lazyInject

/**
 * @author Jordan Abraham
 */
lazyInject<PacketDisassemblerListener>().disassemblePacket(opcode = 44, size = -1) {
    ClientCheatPacket(
        command = readStringCp1252NullTerminated()
    )
}
