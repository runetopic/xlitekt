package script.packet.assembler

import xlitekt.game.packet.UpdateWeightPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.writeShort

/**
 * @author Jordan Abraham
 */
onPacketAssembler<UpdateWeightPacket>(opcode = 50, size = 2) {
    it.writeShort(weight.toInt())
}
