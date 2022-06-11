package script.packet.assembler

import xlitekt.game.packet.UpdatePrivateChatStatusPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.buildFixedPacket
import xlitekt.shared.buffer.writeByte

/**
 * @author Jordan Abraham
 */
onPacketAssembler<UpdatePrivateChatStatusPacket>(opcode = 85, size = 1) {
    buildFixedPacket(1) {
        writeByte(mode)
    }
}
