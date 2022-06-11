package script.packet.assembler

import xlitekt.game.packet.SoundEffectPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.buildFixedPacket
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.buffer.writeShort

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
onPacketAssembler<SoundEffectPacket>(opcode = 81, size = 5) {
    buildFixedPacket(5) {
        writeShort(id)
        writeByte(count)
        writeShort(delay)
    }
}
