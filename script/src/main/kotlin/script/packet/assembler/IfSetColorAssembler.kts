package script.packet.assembler

import xlitekt.game.packet.IfSetColorPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.buildFixedPacket
import xlitekt.shared.buffer.writeIntV2
import xlitekt.shared.buffer.writeShortLittleEndian

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
onPacketAssembler<IfSetColorPacket>(opcode = 62, size = 6) {
    buildFixedPacket(6) {
        writeIntV2(packedInterface)
        writeShortLittleEndian(color)
    }
}
