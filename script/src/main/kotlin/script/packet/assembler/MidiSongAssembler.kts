package script.packet.assembler

import xlitekt.game.packet.MidiSongPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.allocate
import xlitekt.shared.buffer.writeShort

/**
 * @author Jordan Abraham
 */
onPacketAssembler<MidiSongPacket>(opcode = 45, size = 2) {
    allocate(2) {
        writeShort { songId }
    }
}
