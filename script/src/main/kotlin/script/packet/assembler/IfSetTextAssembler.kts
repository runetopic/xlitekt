package script.packet.assembler

import io.ktor.utils.io.core.buildPacket
import xlitekt.game.packet.IfSetTextPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.writeIntLittleEndian
import xlitekt.shared.buffer.writeStringCp1252NullTerminated

/**
 * @author Jordan Abraham
 */
onPacketAssembler<IfSetTextPacket>(opcode = 17, size = -2) {
    buildPacket {
        writeStringCp1252NullTerminated { text }
        writeIntLittleEndian { packedInterface }
    }
}
