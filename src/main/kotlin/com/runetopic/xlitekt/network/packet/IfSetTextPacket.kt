package com.runetopic.xlitekt.network.packet

/**
 * @author Jordan Abraham
 */
data class IfSetTextPacket(
    val packedInterface: Int,
    val text: String,
) : Packet
