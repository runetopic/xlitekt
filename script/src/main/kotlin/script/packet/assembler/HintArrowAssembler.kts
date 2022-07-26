package script.packet.assembler

import xlitekt.game.actor.render.HintArrowType
import xlitekt.game.packet.HintArrowPacket
import xlitekt.game.packet.assembler.PacketAssemblerListener
import xlitekt.shared.buffer.fill
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.buffer.writeShort
import xlitekt.shared.insert

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
insert<PacketAssemblerListener>().assemblePacket<HintArrowPacket>(opcode = 43, size = 6) {
    it.writeByte(type.id)

    when (type) {
        HintArrowType.PLAYER, HintArrowType.NPC -> {
            it.writeShort(targetIndex)
            it.fill(3, 0)
        }
        HintArrowType.LOCATION -> {
            it.writeShort(targetX)
            it.writeShort(targetZ)
            it.writeByte(targetHeight)
        }
    }
}
