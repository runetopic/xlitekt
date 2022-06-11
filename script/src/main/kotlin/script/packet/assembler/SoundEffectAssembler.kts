package script.packet.assembler

import xlitekt.game.packet.SoundEffectPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.buffer.writeShort

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
onPacketAssembler<SoundEffectPacket>(opcode = 81, size = 5) {
    it.writeShort(id)
    it.writeByte(count)
    it.writeShort(delay)
}
