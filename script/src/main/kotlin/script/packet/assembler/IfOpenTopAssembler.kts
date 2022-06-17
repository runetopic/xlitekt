package script.packet.assembler

import xlitekt.game.packet.IfOpenTopPacket
import xlitekt.game.packet.assembler.PacketAssemblerListener
import xlitekt.shared.buffer.writeShort
import xlitekt.shared.lazyInject

/**
 * @author Jordan Abraham
 */
lazyInject<PacketAssemblerListener>().assemblePacket<IfOpenTopPacket>(opcode = 31, size = 2) {
    it.writeShort(interfaceId)
}
