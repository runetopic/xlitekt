package script.packet.assembler

import xlitekt.game.packet.IfSetHiddenPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.allocate
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.buffer.writeIntV1
import xlitekt.shared.toInt

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
onPacketAssembler<IfSetHiddenPacket>(opcode = 57, size = 5) {
    allocate(5) {
        writeByte(hidden::toInt)
        writeIntV1 { packedInterface }
    }
}
