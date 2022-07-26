package script.packet.assembler

import xlitekt.game.packet.UpdateRunWeightPacket
import xlitekt.game.packet.assembler.PacketAssemblerListener
import xlitekt.shared.buffer.writeShort
import xlitekt.shared.insert

/**
 * @author Jordan Abraham
 */
insert<PacketAssemblerListener>().assemblePacket<UpdateRunWeightPacket>(opcode = 50, size = 2) {
    it.writeShort(weight.toInt())
}
