package com.runetopic.xlitekt.network.packet.assembler

import com.runetopic.xlitekt.network.packet.MidiSongPacket
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeShort

/**
 * @author Jordan Abraham
 */
class MidiSongPacketAssembler : PacketAssembler<MidiSongPacket>(opcode = 45, size = 2) {
    override fun assemblePacket(packet: MidiSongPacket) = buildPacket {
        writeShort(packet.songId.toShort())
    }
}
