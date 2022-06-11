package script.packet.assembler

import xlitekt.game.packet.UpdatePublicChatStatusPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.buildFixedPacket
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.buffer.writeByteNegate

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
onPacketAssembler<UpdatePublicChatStatusPacket>(opcode = 32, size = 2) {
    buildFixedPacket(2) {
        writeByte(chatMode)
        writeByteNegate(tradeMode)
    }
}
