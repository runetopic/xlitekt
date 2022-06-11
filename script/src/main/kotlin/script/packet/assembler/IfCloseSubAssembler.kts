package script.packet.assembler

import xlitekt.game.packet.IfCloseSubPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.buildFixedPacket
import xlitekt.shared.buffer.writeInt

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
onPacketAssembler<IfCloseSubPacket>(opcode = 13, size = 4) {
    buildFixedPacket(4) {
        writeInt(packedInterface)
    }
}
