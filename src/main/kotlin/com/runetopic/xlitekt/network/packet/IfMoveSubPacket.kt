package com.runetopic.xlitekt.network.packet

/**
 * @author Jordan Abraham
 */
data class IfMoveSubPacket(
    val fromHash: Int,
    val toHash: Int,
) : Packet
