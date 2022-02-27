package xlitekt.plugin.packet.assembler

import io.ktor.utils.io.core.buildPacket
import xlitekt.game.packet.UpdatePublicChatStatusPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.writeByteNegate

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
onPacketAssembler<UpdatePublicChatStatusPacket>(opcode = 32, size = 2) {
    buildPacket {
        writeByte(chatMode.toByte())
        writeByteNegate(tradeMode.toByte())
    }
}
