package plugin.packet.disassembler

import xlitekt.game.packet.OpObjPacket
import xlitekt.game.packet.disassembler.onPacketDisassembler
import xlitekt.shared.buffer.readUShortLittleEndian

/**
 * @author Jordan Abraham
 */
onPacketDisassembler(opcode = 100, size = 2) {
    OpObjPacket(
        index = 6,
        packedInterface = -1,
        slotId = 0xffff,
        itemId = readUShortLittleEndian()
    )
}
