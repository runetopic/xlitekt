package com.runetopic.xlitekt.plugin.script.packet.assembler

import com.runetopic.xlitekt.network.packet.MidiSongPacket
import com.runetopic.xlitekt.network.packet.assembler.onPacketAssembler
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeShort

/**
 * @author Jordan Abraham
 */
onPacketAssembler<MidiSongPacket>(opcode = 45, size = 2) {
    buildPacket {
        writeShort(songId.toShort())
    }
}
