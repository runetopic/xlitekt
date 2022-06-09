package script.packet.assembler

import xlitekt.game.packet.IfSetColorPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.allocate
import xlitekt.shared.buffer.writeIntV2
import xlitekt.shared.buffer.writeShortLittleEndian

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
onPacketAssembler<IfSetColorPacket>(opcode = 62, size = 6) {
    allocate(6) {
        writeIntV2(packedInterface)
        writeShortLittleEndian(color)
    }
}
