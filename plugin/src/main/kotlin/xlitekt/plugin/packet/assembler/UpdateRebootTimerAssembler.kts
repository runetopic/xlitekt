package xlitekt.plugin.packet.assembler

import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeShort
import xlitekt.game.packet.UpdateRebootTimerPacket
import xlitekt.game.packet.assembler.onPacketAssembler

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
onPacketAssembler<UpdateRebootTimerPacket>(opcode = 15, size = 2) {
    buildPacket {
        writeShort(rebootTimer.toShort())
    }
}
