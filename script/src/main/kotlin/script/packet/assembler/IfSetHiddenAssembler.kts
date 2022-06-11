package script.packet.assembler

import xlitekt.game.packet.IfSetHiddenPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.buffer.writeIntV1
import xlitekt.shared.toInt

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
onPacketAssembler<IfSetHiddenPacket>(opcode = 57, size = 5) {
    it.writeByte(hidden.toInt())
    it.writeIntV1(packedInterface)
}
