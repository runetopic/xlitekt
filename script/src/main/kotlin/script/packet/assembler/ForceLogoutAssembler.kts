package script.packet.assembler

import xlitekt.game.packet.ForceLogoutPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.allocate

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
onPacketAssembler<ForceLogoutPacket>(opcode = 87, size = 0) {
    allocate(0) { }
}
