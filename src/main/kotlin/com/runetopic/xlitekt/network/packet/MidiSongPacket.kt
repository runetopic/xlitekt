package com.runetopic.xlitekt.network.packet

/**
 * @author Jordan Abraham
 */
data class MidiSongPacket(
    val songId: Int
) : Packet
