package script.packet.disassembler

import xlitekt.game.packet.WindowStatusPacket
import xlitekt.game.packet.disassembler.PacketDisassemblerListener
import xlitekt.shared.buffer.readUShort
import xlitekt.shared.lazyInject

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
lazyInject<PacketDisassemblerListener>().disassemblePacket(opcode = 48, size = 5) {
    WindowStatusPacket(
        displayMode = readByte().toInt(),
        width = readUShort(),
        height = readUShort()
    )
}
