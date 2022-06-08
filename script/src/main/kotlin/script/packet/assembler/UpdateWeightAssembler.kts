package script.packet.assembler

import io.ktor.utils.io.core.buildPacket
import xlitekt.game.packet.UpdateWeightPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.writeShort

/**
 * @author Jordan Abraham
 */
onPacketAssembler<UpdateWeightPacket>(opcode = 50, size = 2) {
    buildPacket {
        writeShort(weight::toInt)
    }
}
