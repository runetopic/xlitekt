package script.packet.disassembler

import xlitekt.game.packet.IfButtonPacket
import xlitekt.game.packet.disassembler.PacketDisassemblerListener
import xlitekt.shared.buffer.readUShort
import xlitekt.shared.lazyInject

/**
 * @author Jordan Abraham
 */
lazyInject<PacketDisassemblerListener>().disassemblePacket(opcode = 11, size = 8) {
    IfButtonPacket(
        index = 2,
        packedInterface = readInt(),
        slotId = readUShort(),
        itemId = readUShort()
    )
}
