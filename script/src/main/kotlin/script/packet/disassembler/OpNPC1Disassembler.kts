package script.packet.disassembler

import xlitekt.game.packet.OpNPCPacket
import xlitekt.game.packet.disassembler.PacketDisassemblerListener
import xlitekt.shared.buffer.readUByteSubtract
import xlitekt.shared.buffer.readUShortSubtract
import xlitekt.shared.insert
import xlitekt.shared.toBoolean

/**
 * @author Jordan Abraham
 */
insert<PacketDisassemblerListener>().disassemblePacket(opcode = 37, size = 3) {
    OpNPCPacket(
        index = 1,
        npcIndex = readUShortSubtract(),
        running = readUByteSubtract().toBoolean()
    )
}
