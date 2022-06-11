package script.packet.assembler

import xlitekt.game.packet.UpdateStatPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.buildFixedPacket
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.buffer.writeByteSubtract
import xlitekt.shared.buffer.writeIntV1

/**
 * @author Jordan Abraham
 */
onPacketAssembler<UpdateStatPacket>(opcode = 34, size = 6) {
    buildFixedPacket(6) {
        writeByte(level)
        writeByteSubtract(skillId)
        writeIntV1(xp.toInt())
    }
}
