package script.packet.assembler

import io.ktor.utils.io.core.buildPacket
import xlitekt.game.packet.IfOpenSubPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.writeByteSubtract
import xlitekt.shared.buffer.writeInt
import xlitekt.shared.buffer.writeShortLittleEndian
import xlitekt.shared.toInt

/**
 * @author Jordan Abraham
 */
onPacketAssembler<IfOpenSubPacket>(opcode = 2, size = 7) {
    buildPacket {
        writeShortLittleEndian { interfaceId }
        writeByteSubtract(walkable::toInt)
        writeInt { toPackedInterface }
    }
}
