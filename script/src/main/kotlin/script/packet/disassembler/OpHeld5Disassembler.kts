package script.packet.disassembler

import xlitekt.game.packet.OpHeldPacket
import xlitekt.game.packet.disassembler.PacketDisassemblerListener
import xlitekt.shared.buffer.readIntV2
import xlitekt.shared.buffer.readUShort
import xlitekt.shared.buffer.readUShortLittleEndianSubtract
import xlitekt.shared.lazyInject

/**
 * @author Jordan Abraham
 */
lazyInject<PacketDisassemblerListener>().disassemblePacket(opcode = 43, size = 8) {
    OpHeldPacket(
        index = 5,
        fromPackedInterface = readIntV2(),
        fromSlotId = readUShortLittleEndianSubtract(),
        fromItemId = readUShort(),
        toPackedInterface = -1,
        toSlotId = 0xffff,
        toItemId = 0xffff
    )
}
