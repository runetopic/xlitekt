package xlitekt.plugin.packet.assembler

import io.ktor.utils.io.core.buildPacket
import xlitekt.game.packet.UpdateRunEnergyPacket
import xlitekt.game.packet.assembler.onPacketAssembler

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
onPacketAssembler<UpdateRunEnergyPacket>(opcode = 59, size = 1) {
    buildPacket {
        writeByte(energy.toInt().toByte())
    }
}
