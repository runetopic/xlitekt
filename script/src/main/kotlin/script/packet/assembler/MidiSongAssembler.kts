package script.packet.assembler

import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeShort
import xlitekt.game.packet.MidiSongPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.writeShort

/**
 * @author Jordan Abraham
 */
onPacketAssembler<MidiSongPacket>(opcode = 45, size = 2) {
    buildPacket {
        writeShort { songId }
    }
}
