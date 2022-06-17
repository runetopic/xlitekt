package script.packet.assembler

import xlitekt.game.packet.IfSetColorPacket
import xlitekt.game.packet.assembler.PacketAssemblerListener
import xlitekt.shared.buffer.writeIntV2
import xlitekt.shared.buffer.writeShortLittleEndian
import xlitekt.shared.lazyInject

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
lazyInject<PacketAssemblerListener>().assemblePacket<IfSetColorPacket>(opcode = 62, size = 6) {
    it.writeIntV2(packedInterface)
    it.writeShortLittleEndian(color)
}
