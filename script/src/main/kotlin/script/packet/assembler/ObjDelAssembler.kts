package script.packet.assembler

import xlitekt.game.packet.ObjDelPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.allocate
import xlitekt.shared.buffer.writeByteAdd
import xlitekt.shared.buffer.writeShortLittleEndian

/**
 * @author Jordan Abraham
 */
onPacketAssembler<ObjDelPacket>(opcode = 26, size = 3) {
    allocate(3) {
        writeByteAdd { packedOffset }
        writeShortLittleEndian { id }
    }
}
