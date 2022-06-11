package script.packet.assembler

import xlitekt.game.packet.CamResetPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.buildFixedPacket

/**
 * @author Jordan Abraham
 */
onPacketAssembler<CamResetPacket>(opcode = 35, size = 0) {
    buildFixedPacket(0) { }
}
