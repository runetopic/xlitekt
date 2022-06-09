package script.packet.assembler

import xlitekt.game.packet.SetMapFlagPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.allocate
import xlitekt.shared.buffer.writeByte

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
onPacketAssembler<SetMapFlagPacket>(opcode = 93, size = 2) {
    allocate(2) {
        writeByte(destinationX)
        writeByte(destinationZ)
    }
}
