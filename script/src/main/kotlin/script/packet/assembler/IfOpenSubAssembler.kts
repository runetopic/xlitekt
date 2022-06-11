package script.packet.assembler

import xlitekt.game.packet.IfOpenSubPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.buildFixedPacket
import xlitekt.shared.buffer.writeByteSubtract
import xlitekt.shared.buffer.writeInt
import xlitekt.shared.buffer.writeShortLittleEndian
import xlitekt.shared.toInt

/**
 * @author Jordan Abraham
 */
onPacketAssembler<IfOpenSubPacket>(opcode = 2, size = 7) {
    buildFixedPacket(7) {
        writeShortLittleEndian(interfaceId)
        writeByteSubtract(walkable.toInt())
        writeInt(toPackedInterface)
    }
}
