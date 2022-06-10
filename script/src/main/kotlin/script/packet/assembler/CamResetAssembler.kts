package script.packet.assembler

import xlitekt.game.packet.CamResetPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.allocate

/**
 * @author Jordan Abraham
 */
onPacketAssembler<CamResetPacket>(opcode = 35, size = 0) {
    allocate(0) { }
}
