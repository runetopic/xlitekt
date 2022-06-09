package script.packet.disassembler

import xlitekt.game.packet.IfButtonPacket
import xlitekt.game.packet.disassembler.onPacketDisassembler
import xlitekt.shared.buffer.readInt
import xlitekt.shared.buffer.readUShort

/**
 * @author Jordan Abraham
 */
onPacketDisassembler(opcode = 20, size = 8) {
    IfButtonPacket(
        index = 7,
        packedInterface = readInt(),
        slotId = readUShort(),
        itemId = readUShort()
    )
}
