package script.packet.assembler

import xlitekt.game.packet.IfSetTextPacket
import xlitekt.game.packet.assembler.PacketAssemblerListener
import xlitekt.shared.buffer.writeIntLittleEndian
import xlitekt.shared.buffer.writeStringCp1252NullTerminated
import xlitekt.shared.insert

/**
 * @author Jordan Abraham
 */
insert<PacketAssemblerListener>().assemblePacket<IfSetTextPacket>(opcode = 17, size = -2) {
    it.writeStringCp1252NullTerminated(text)
    it.writeIntLittleEndian(packedInterface)
}
