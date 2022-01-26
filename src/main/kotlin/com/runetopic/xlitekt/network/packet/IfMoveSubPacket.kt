package com.runetopic.xlitekt.network.packet

/**
 * @author Tyler Telis
 */
data class IfMoveSubPacket(
    val fromHash: Int,
    val toHash: Int,
) : Packet
