package script.packet.assembler

import xlitekt.game.packet.IfSetTextPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.allocate
import xlitekt.shared.buffer.writeIntLittleEndian
import xlitekt.shared.buffer.writeStringCp1252NullTerminated

/**
 * @author Jordan Abraham
 */
onPacketAssembler<IfSetTextPacket>(opcode = 17, size = -2) {
    allocate(text.length + 1 + 4) {
        writeStringCp1252NullTerminated { text }
        writeIntLittleEndian { packedInterface }
    }
}
