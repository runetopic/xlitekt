package script.packet.disassembler

import xlitekt.game.packet.OpHeldPacket
import xlitekt.game.packet.disassembler.onPacketDisassembler
import xlitekt.shared.buffer.readIntLittleEndian
import xlitekt.shared.buffer.readUShortLittleEndian

/**
 * @author Jordan Abraham
 */
onPacketDisassembler(opcode = 31, size = 8) {
    OpHeldPacket(
        index = 2,
        fromItemId = readUShortLittleEndian(),
        fromSlotId = readUShortLittleEndian(),
        fromPackedInterface = readIntLittleEndian(),
        toPackedInterface = -1,
        toSlotId = 0xffff,
        toItemId = 0xffff
    )
}
