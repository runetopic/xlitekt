package script.packet.assembler

import xlitekt.game.packet.UpdatePublicChatStatusPacket
import xlitekt.game.packet.assembler.PacketAssemblerListener
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.buffer.writeByteNegate
import xlitekt.shared.lazyInject

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
lazyInject<PacketAssemblerListener>().assemblePacket<UpdatePublicChatStatusPacket>(opcode = 32, size = 2) {
    it.writeByte(chatMode)
    it.writeByteNegate(tradeMode)
}
