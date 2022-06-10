package script.packet.assembler

import xlitekt.game.actor.render.HintArrowType
import xlitekt.game.packet.HintArrowPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.allocate
import xlitekt.shared.buffer.fill
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.buffer.writeShort

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
onPacketAssembler<HintArrowPacket>(opcode = 43, size = 6) {
    allocate(6) {
        writeByte(type.id)

        when (type) {
            HintArrowType.PLAYER, HintArrowType.NPC -> {
                writeShort(targetIndex)
                fill(3, 0)
            }
            HintArrowType.LOCATION -> {
                writeShort(targetX)
                writeShort(targetZ)
                writeByte(targetHeight)
            }
        }
    }
}
