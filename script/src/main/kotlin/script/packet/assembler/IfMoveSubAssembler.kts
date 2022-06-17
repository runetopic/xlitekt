package script.packet.assembler

import xlitekt.game.packet.IfMoveSubPacket
import xlitekt.game.packet.assembler.PacketAssemblerListener
import xlitekt.shared.buffer.writeInt
import xlitekt.shared.lazyInject

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
lazyInject<PacketAssemblerListener>().assemblePacket<IfMoveSubPacket>(opcode = 30, size = 8) {
    it.writeInt(fromPackedInterface)
    it.writeInt(toPackedInterface)
}
