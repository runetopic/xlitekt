package script.packet.assembler

import xlitekt.game.packet.UpdatePrivateChatStatusPacket
import xlitekt.game.packet.assembler.PacketAssemblerListener
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.lazyInject

/**
 * @author Jordan Abraham
 */
lazyInject<PacketAssemblerListener>().assemblePacket<UpdatePrivateChatStatusPacket>(opcode = 85, size = 1) {
    it.writeByte(mode)
}
