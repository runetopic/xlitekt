package script.packet.assembler

import xlitekt.game.packet.UpdatePrivateChatStatusPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.allocate
import xlitekt.shared.buffer.writeByte

/**
 * @author Jordan Abraham
 */
onPacketAssembler<UpdatePrivateChatStatusPacket>(opcode = 85, size = 1) {
    allocate(1) {
        writeByte { mode }
    }
}
