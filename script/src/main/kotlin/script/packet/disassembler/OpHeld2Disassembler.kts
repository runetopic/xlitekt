package script.packet.disassembler

import xlitekt.game.packet.OpHeldPacket
import xlitekt.game.packet.disassembler.PacketDisassemblerListener
import xlitekt.shared.buffer.readIntLittleEndian
import xlitekt.shared.buffer.readUShortLittleEndian
import xlitekt.shared.lazyInject

/**
 * @author Jordan Abraham
 */
lazyInject<PacketDisassemblerListener>().disassemblePacket(opcode = 31, size = 8) {
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
