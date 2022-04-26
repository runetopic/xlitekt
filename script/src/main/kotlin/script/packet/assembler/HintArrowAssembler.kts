package script.packet.assembler

import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.fill
import io.ktor.utils.io.core.writeShort
import xlitekt.game.actor.render.HintArrowType
import xlitekt.game.packet.HintArrowPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.buffer.writeShort

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
onPacketAssembler<HintArrowPacket>(opcode = 43, size = 6) {
    buildPacket {
        writeByte(type.id.toByte())

        when (type) {
            HintArrowType.PLAYER, HintArrowType.NPC -> {
                writeShort { targetIndex }
                fill(3, 0)
            }
            HintArrowType.LOCATION -> {
                writeShort { targetX }
                writeShort { targetZ }
                writeByte { targetHeight }
            }
        }
    }
}
