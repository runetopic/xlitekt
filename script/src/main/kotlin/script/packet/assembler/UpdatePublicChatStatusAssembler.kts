package script.packet.assembler

import xlitekt.game.packet.UpdatePublicChatStatusPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.allocate
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.buffer.writeByteNegate

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
onPacketAssembler<UpdatePublicChatStatusPacket>(opcode = 32, size = 2) {
    allocate(2) {
        writeByte { chatMode }
        writeByteNegate { tradeMode }
    }
}
