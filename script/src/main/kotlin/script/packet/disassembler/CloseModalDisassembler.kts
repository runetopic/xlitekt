package script.packet.disassembler

import xlitekt.game.packet.CloseModalPacket
import xlitekt.game.packet.disassembler.PacketDisassemblerListener
import xlitekt.shared.lazyInject

/**
 * @author Jordan Abraham
 */
lazyInject<PacketDisassemblerListener>().disassemblePacket(opcode = 96, size = 0) {
    CloseModalPacket()
}
