package script.packet.assembler

import xlitekt.game.packet.UpdateRunEnergyPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.allocate
import xlitekt.shared.buffer.writeByte

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
onPacketAssembler<UpdateRunEnergyPacket>(opcode = 59, size = 1) {
    allocate(1) {
        writeByte(energy::toInt)
    }
}
