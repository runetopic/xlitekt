package script.packet.assembler

import xlitekt.game.packet.IfMoveSubPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.buildFixedPacket
import xlitekt.shared.buffer.writeInt

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
onPacketAssembler<IfMoveSubPacket>(opcode = 30, size = 8) {
    buildFixedPacket(8) {
        writeInt(fromPackedInterface)
        writeInt(toPackedInterface)
    }
}
