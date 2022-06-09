package script.packet.assembler

import xlitekt.game.packet.UpdateRebootTimerPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.allocate
import xlitekt.shared.buffer.writeShort

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
onPacketAssembler<UpdateRebootTimerPacket>(opcode = 15, size = 2) {
    allocate(2) {
        writeShort(rebootTimer)
    }
}
