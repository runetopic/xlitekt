package script.packet.assembler

import xlitekt.game.packet.NoTimeoutPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.buildFixedPacket

/**
 * @author Jordan Abraham
 */
onPacketAssembler<NoTimeoutPacket>(opcode = 58, size = 0) {
    buildFixedPacket(0) { }
}
