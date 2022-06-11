package script.packet.assembler

import xlitekt.game.packet.VarpSmallPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.buildFixedPacket
import xlitekt.shared.buffer.writeByteAdd
import xlitekt.shared.buffer.writeShort

/**
 * @author Jordan Abraham
 */
onPacketAssembler<VarpSmallPacket>(opcode = 94, size = 3) {
    buildFixedPacket(3) {
        writeByteAdd(value)
        writeShort(id)
    }
}
