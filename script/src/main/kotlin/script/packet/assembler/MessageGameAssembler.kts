package script.packet.assembler

import xlitekt.game.packet.MessageGamePacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.buildDynamicPacket
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.buffer.writeSmart
import xlitekt.shared.buffer.writeStringCp1252NullTerminated
import xlitekt.shared.toInt

/**
 * @author Jordan Abraham
 */
onPacketAssembler<MessageGamePacket>(opcode = 69, size = -1) {
    buildDynamicPacket {
        writeSmart(type)
        writeByte(hasPrefix.toInt())
        if (hasPrefix) writeStringCp1252NullTerminated(prefix)
        writeStringCp1252NullTerminated(message)
    }
}
