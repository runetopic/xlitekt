package script.packet.assembler

import xlitekt.game.packet.IfSetHiddenPacket
import xlitekt.game.packet.assembler.PacketAssemblerListener
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.buffer.writeIntV1
import xlitekt.shared.insert
import xlitekt.shared.toInt

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
insert<PacketAssemblerListener>().assemblePacket<IfSetHiddenPacket>(opcode = 57, size = 5) {
    it.writeByte(hidden.toInt())
    it.writeIntV1(packedInterface)
}
