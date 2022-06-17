package script.packet.assembler

import xlitekt.game.packet.MiniMapTogglePacket
import xlitekt.game.packet.assembler.PacketAssemblerListener
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.lazyInject

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
lazyInject<PacketAssemblerListener>().assemblePacket<MiniMapTogglePacket>(opcode = 36, size = 1) {
    it.writeByte(type)
}
