package script.packet.disassembler

import xlitekt.game.packet.ExamineItemPacket
import xlitekt.game.packet.disassembler.PacketDisassemblerListener
import xlitekt.shared.buffer.readUShortLittleEndian
import xlitekt.shared.lazyInject

/**
 * @author Justin Kenney
 */
lazyInject<PacketDisassemblerListener>().disassemblePacket(opcode = 100, size = 2) {
    ExamineItemPacket(
        itemId = readUShortLittleEndian()
    )
}
