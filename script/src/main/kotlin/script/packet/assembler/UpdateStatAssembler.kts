package script.packet.assembler

import xlitekt.game.packet.UpdateStatPacket
import xlitekt.game.packet.assembler.PacketAssemblerListener
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.buffer.writeByteSubtract
import xlitekt.shared.buffer.writeIntV1
import xlitekt.shared.insert

/**
 * @author Jordan Abraham
 */
insert<PacketAssemblerListener>().assemblePacket<UpdateStatPacket>(opcode = 34, size = 6) {
    it.writeByte(level)
    it.writeByteSubtract(skillId)
    it.writeIntV1(xp.toInt())
}
