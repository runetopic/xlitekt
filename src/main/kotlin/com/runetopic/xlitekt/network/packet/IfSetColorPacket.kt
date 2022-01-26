package com.runetopic.xlitekt.network.packet

/**
 * @author Jordan Abraham
 */
data class IfSetColorPacket(
    val packedInterface: Int,
    val color: Int,
) : Packet
