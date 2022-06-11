package script.packet.assembler

import xlitekt.game.packet.MidiSongPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.buildFixedPacket
import xlitekt.shared.buffer.writeShort

/**
 * @author Jordan Abraham
 */
onPacketAssembler<MidiSongPacket>(opcode = 45, size = 2) {
    buildFixedPacket(2) {
        writeShort(songId)
    }
}
