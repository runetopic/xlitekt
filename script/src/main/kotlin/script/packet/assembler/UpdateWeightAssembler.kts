package script.packet.assembler

import xlitekt.game.packet.UpdateWeightPacket
import xlitekt.game.packet.assembler.PacketAssemblerListener
import xlitekt.shared.buffer.writeShort
import xlitekt.shared.insert

/**
 * @author Jordan Abraham
 */
insert<PacketAssemblerListener>().assemblePacket<UpdateWeightPacket>(opcode = 50, size = 2) {
    it.writeShort(weight.toInt())
}
