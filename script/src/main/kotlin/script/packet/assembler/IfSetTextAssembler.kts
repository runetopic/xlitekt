package script.packet.assembler

import xlitekt.game.packet.IfSetTextPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.writeIntLittleEndian
import xlitekt.shared.buffer.writeStringCp1252NullTerminated

/**
 * @author Jordan Abraham
 */
onPacketAssembler<IfSetTextPacket>(opcode = 17, size = -2) {
    it.writeStringCp1252NullTerminated(text)
    it.writeIntLittleEndian(packedInterface)
}
