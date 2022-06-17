package script.packet.assembler

import xlitekt.game.packet.MiniMapTogglePacket
import xlitekt.game.packet.assembler.PacketAssemblerListener
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.insert

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
insert<PacketAssemblerListener>().assemblePacket<MiniMapTogglePacket>(opcode = 36, size = 1) {
    it.writeByte(type)
}
