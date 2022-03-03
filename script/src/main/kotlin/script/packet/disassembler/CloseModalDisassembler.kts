package script.packet.disassembler

import xlitekt.game.packet.CloseModalPacket
import xlitekt.game.packet.disassembler.onPacketDisassembler

/**
 * @author Jordan Abraham
 */
onPacketDisassembler(opcode = 96, size = 0) {
    CloseModalPacket()
}
