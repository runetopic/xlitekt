package script.packet.disassembler

import xlitekt.game.packet.OpLocPacket
import xlitekt.game.packet.disassembler.PacketDisassemblerListener
import xlitekt.shared.buffer.readUByteNegate
import xlitekt.shared.buffer.readUShort
import xlitekt.shared.buffer.readUShortLittleEndian
import xlitekt.shared.buffer.readUShortLittleEndianAdd
import xlitekt.shared.insert
import xlitekt.shared.toBoolean

/**
 * @author Jordan Abraham
 */
insert<PacketDisassemblerListener>().disassemblePacket(opcode = 81, size = 7) {
    OpLocPacket(
        index = 1,
        x = readUShortLittleEndian(),
        locId = readUShort(),
        z = readUShortLittleEndianAdd(),
        running = readUByteNegate().toBoolean()
    )
}
