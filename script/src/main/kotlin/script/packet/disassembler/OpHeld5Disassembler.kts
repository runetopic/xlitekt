package script.packet.disassembler

import io.ktor.utils.io.core.readUShort
import xlitekt.game.packet.OpHeldPacket
import xlitekt.game.packet.disassembler.onPacketDisassembler
import xlitekt.shared.buffer.readIntV2
import xlitekt.shared.buffer.readUShortLittleEndianSubtract

/**
 * @author Jordan Abraham
 */
onPacketDisassembler(opcode = 43, size = 8) {
    OpHeldPacket(
        index = 5,
        fromPackedInterface = readIntV2(),
        fromSlotId = readUShortLittleEndianSubtract(),
        fromItemId = readUShort().toInt(),
        toPackedInterface = -1,
        toSlotId = 0xffff,
        toItemId = 0xffff
    )
}
