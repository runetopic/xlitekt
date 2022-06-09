package script.packet.assembler

import xlitekt.game.packet.VarpSmallPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.allocate
import xlitekt.shared.buffer.writeByteAdd
import xlitekt.shared.buffer.writeShort

/**
 * @author Jordan Abraham
 */
onPacketAssembler<VarpSmallPacket>(opcode = 94, size = 3) {
    allocate(3) {
        writeByteAdd(value)
        writeShort(id)
    }
}
