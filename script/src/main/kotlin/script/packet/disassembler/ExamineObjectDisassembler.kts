package script.packet.disassembler

import xlitekt.game.packet.ExamineObjectPacket
import xlitekt.game.packet.disassembler.PacketDisassemblerListener
import xlitekt.shared.buffer.readUShortAdd
import xlitekt.shared.lazyInject

/**
 * @author Justin Kenney
 */
lazyInject<PacketDisassemblerListener>().disassemblePacket(opcode = 13, size = 2) {
    ExamineObjectPacket(
        objectID = readUShortAdd()
    )
}
