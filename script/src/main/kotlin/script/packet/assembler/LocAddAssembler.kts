package script.packet.assembler

import xlitekt.game.packet.LocAddPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.allocate
import xlitekt.shared.buffer.writeByteSubtract
import xlitekt.shared.buffer.writeShortLittleEndianAdd

/**
 * @author Jordan Abraham
 */
onPacketAssembler<LocAddPacket>(opcode = 18, size = 4) {
    allocate(4) {
        writeByteSubtract((shape shl 2) or (rotation and 0x3))
        writeByteSubtract(packedOffset)
        writeShortLittleEndianAdd(id)
    }
}
