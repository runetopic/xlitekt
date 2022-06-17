package script.packet.assembler

import xlitekt.game.packet.SoundEffectPacket
import xlitekt.game.packet.assembler.PacketAssemblerListener
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.buffer.writeShort
import xlitekt.shared.insert

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
insert<PacketAssemblerListener>().assemblePacket<SoundEffectPacket>(opcode = 81, size = 5) {
    it.writeShort(id)
    it.writeByte(count)
    it.writeShort(delay)
}
