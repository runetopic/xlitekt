package script.packet.disassembler

import xlitekt.game.packet.ExamineNPCPacket
import xlitekt.game.packet.disassembler.PacketDisassemblerListener
import xlitekt.shared.buffer.readUShortLittleEndian
import xlitekt.shared.lazyInject

/**
 * @author Justin Kenney
 */
lazyInject<PacketDisassemblerListener>().disassemblePacket(opcode = 22, size = 2) {
    ExamineNPCPacket(
        npcId = readUShortLittleEndian()
    )
}
