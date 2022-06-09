package script.packet.disassembler

import xlitekt.game.packet.IfButtonPacket
import xlitekt.game.packet.disassembler.onPacketDisassembler
import xlitekt.shared.buffer.readInt
import xlitekt.shared.buffer.readUShort

/**
 * @author Jordan Abraham
 */
onPacketDisassembler(opcode = 10, size = 8) {
    IfButtonPacket(
        index = 9,
        packedInterface = readInt(),
        slotId = readUShort(),
        itemId = readUShort()
    )
}
