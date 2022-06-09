package script.packet.assembler

import xlitekt.game.packet.MiniMapTogglePacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.allocate
import xlitekt.shared.buffer.writeByte

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
onPacketAssembler<MiniMapTogglePacket>(opcode = 36, size = 1) {
    allocate(1) {
        writeByte { type }
    }
}
