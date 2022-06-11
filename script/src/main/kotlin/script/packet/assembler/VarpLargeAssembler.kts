package script.packet.assembler

import xlitekt.game.packet.VarpLargePacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.buildFixedPacket
import xlitekt.shared.buffer.writeIntV1
import xlitekt.shared.buffer.writeShortLittleEndianAdd

/**
 * @author Jordan Abraham
 */
onPacketAssembler<VarpLargePacket>(opcode = 0, size = 6) {
    buildFixedPacket(6) {
        writeIntV1(value)
        writeShortLittleEndianAdd(id)
    }
}
