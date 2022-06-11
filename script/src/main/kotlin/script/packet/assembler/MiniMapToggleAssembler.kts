package script.packet.assembler

import xlitekt.game.packet.MiniMapTogglePacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.buildFixedPacket
import xlitekt.shared.buffer.writeByte

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
onPacketAssembler<MiniMapTogglePacket>(opcode = 36, size = 1) {
    buildFixedPacket(1) {
        writeByte(type)
    }
}
