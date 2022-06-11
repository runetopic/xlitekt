package script.packet.assembler

import xlitekt.game.packet.UpdateRebootTimerPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.buildFixedPacket
import xlitekt.shared.buffer.writeShort

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
onPacketAssembler<UpdateRebootTimerPacket>(opcode = 15, size = 2) {
    buildFixedPacket(2) {
        writeShort(rebootTimer)
    }
}
