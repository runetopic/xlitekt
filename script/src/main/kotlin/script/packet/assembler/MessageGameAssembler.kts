package script.packet.assembler

import xlitekt.game.packet.MessageGamePacket
import xlitekt.game.packet.assembler.PacketAssemblerListener
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.buffer.writeSmart
import xlitekt.shared.buffer.writeStringCp1252NullTerminated
import xlitekt.shared.insert
import xlitekt.shared.toInt

/**
 * @author Jordan Abraham
 */
insert<PacketAssemblerListener>().assemblePacket<MessageGamePacket>(opcode = 69, size = -1) {
    it.writeSmart(type)
    it.writeByte(hasPrefix.toInt())
    if (hasPrefix) it.writeStringCp1252NullTerminated(prefix)
    it.writeStringCp1252NullTerminated(message)
}
