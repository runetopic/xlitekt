package script.packet.assembler

import xlitekt.game.packet.UpdateRunEnergyPacket
import xlitekt.game.packet.assembler.PacketAssemblerListener
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.lazyInject

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
lazyInject<PacketAssemblerListener>().assemblePacket<UpdateRunEnergyPacket>(opcode = 59, size = 1) {
    it.writeByte(energy.toInt())
}
