package script.packet.assembler

import xlitekt.game.packet.MidiSongPacket
import xlitekt.game.packet.assembler.PacketAssemblerListener
import xlitekt.shared.buffer.writeShort
import xlitekt.shared.lazyInject

/**
 * @author Jordan Abraham
 */
lazyInject<PacketAssemblerListener>().assemblePacket<MidiSongPacket>(opcode = 45, size = 2) {
    it.writeShort(songId)
}
