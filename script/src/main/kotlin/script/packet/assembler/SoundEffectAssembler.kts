package script.packet.assembler

import xlitekt.game.packet.SoundEffectPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.allocate
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.buffer.writeShort

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
onPacketAssembler<SoundEffectPacket>(opcode = 81, size = 5) {
    allocate(5) {
        writeShort(id)
        writeByte(count)
        writeShort(delay)
    }
}
