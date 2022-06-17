package script.packet.disassembler

import xlitekt.game.packet.OpLocPacket
import xlitekt.game.packet.disassembler.PacketDisassemblerListener
import xlitekt.shared.buffer.readUShortAdd
import xlitekt.shared.buffer.readUShortLittleEndian
import xlitekt.shared.lazyInject
import xlitekt.shared.toBoolean

/**
 * @author Jordan Abraham
 */
lazyInject<PacketDisassemblerListener>().disassemblePacket(opcode = 94, size = 7) {
    OpLocPacket(
        index = 2,
        z = readUShortLittleEndian(),
        running = readByte().toInt().toBoolean(),
        x = readUShortAdd(),
        locId = readUShortLittleEndian()
    )
}
