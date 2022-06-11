package script.packet.assembler

import xlitekt.game.packet.ObjAddPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.buildFixedPacket
import xlitekt.shared.buffer.writeByteSubtract
import xlitekt.shared.buffer.writeShort
import xlitekt.shared.buffer.writeShortLittleEndian

/**
 * @author Jordan Abraham
 */
onPacketAssembler<ObjAddPacket>(opcode = 61, size = 5) {
    buildFixedPacket(5) {
        writeShort(id)
        writeByteSubtract(packedOffset)
        writeShortLittleEndian(amount)
    }
}
