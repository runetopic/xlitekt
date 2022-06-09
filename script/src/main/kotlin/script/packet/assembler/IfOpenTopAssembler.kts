package script.packet.assembler

import xlitekt.game.packet.IfOpenTopPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.allocate
import xlitekt.shared.buffer.writeShort

/**
 * @author Jordan Abraham
 */
onPacketAssembler<IfOpenTopPacket>(opcode = 31, size = 2) {
    allocate(2) {
        writeShort(interfaceId)
    }
}
