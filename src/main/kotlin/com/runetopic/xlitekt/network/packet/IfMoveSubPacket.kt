package com.runetopic.xlitekt.network.packet

/**
 * @author Tyler Telis
 */
data class IfMoveSubPacket(
    val fromPackedInterface: Int,
    val toPackedInterface: Int,
) : Packet
