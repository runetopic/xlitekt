package script.packet.assembler

import xlitekt.game.packet.LogoutPacket
import xlitekt.game.packet.assembler.PacketAssemblerListener
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.lazyInject

/**
 * @author Jordan Abraham
 */
lazyInject<PacketAssemblerListener>().assemblePacket<LogoutPacket>(opcode = 20, size = 1) {
    it.writeByte(type)
}
