package script.packet.assembler

import io.ktor.utils.io.core.buildPacket
import xlitekt.game.packet.UpdateRunEnergyPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.writeByte

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
onPacketAssembler<UpdateRunEnergyPacket>(opcode = 59, size = 1) {
    buildPacket {
        writeByte(energy::toInt)
    }
}
