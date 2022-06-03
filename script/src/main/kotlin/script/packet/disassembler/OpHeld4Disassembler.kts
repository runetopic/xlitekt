package script.packet.disassembler

import xlitekt.game.packet.OpHeldPacket
import xlitekt.game.packet.disassembler.onPacketDisassembler
import xlitekt.shared.buffer.readIntV1
import xlitekt.shared.buffer.readUShortAdd
import xlitekt.shared.buffer.readUShortLittleEndian

/**
 * @author Justin Kenney
 */
onPacketDisassembler(opcode = 5, size = 8) {
    OpHeldPacket(
        index = 4,
        fromPackedInterface = readIntV1(),
        fromSlotId = readUShortAdd(),
        fromItemId = readUShortLittleEndian(),
        toPackedInterface = -1,
        toSlotId = 0xffff,
        toItemId = 0xffff
    )
}
