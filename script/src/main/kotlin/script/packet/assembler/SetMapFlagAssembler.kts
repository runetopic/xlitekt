package script.packet.assembler

import xlitekt.game.packet.SetMapFlagPacket
import xlitekt.game.packet.assembler.PacketAssemblerListener
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.insert

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
insert<PacketAssemblerListener>().assemblePacket<SetMapFlagPacket>(opcode = 93, size = 2) {
    it.writeByte(destinationX)
    it.writeByte(destinationZ)
}
