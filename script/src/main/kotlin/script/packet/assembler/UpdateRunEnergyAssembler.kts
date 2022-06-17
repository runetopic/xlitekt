package script.packet.assembler

import xlitekt.game.packet.UpdateRunEnergyPacket
import xlitekt.game.packet.assembler.PacketAssemblerListener
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.insert

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
insert<PacketAssemblerListener>().assemblePacket<UpdateRunEnergyPacket>(opcode = 59, size = 1) {
    it.writeByte(energy.toInt())
}
