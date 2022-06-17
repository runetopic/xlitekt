package script.packet.assembler

import xlitekt.game.packet.UpdateWeightPacket
import xlitekt.game.packet.assembler.PacketAssemblerListener
import xlitekt.shared.buffer.writeShort
import xlitekt.shared.lazyInject

/**
 * @author Jordan Abraham
 */
lazyInject<PacketAssemblerListener>().assemblePacket<UpdateWeightPacket>(opcode = 50, size = 2) {
    it.writeShort(weight.toInt())
}
