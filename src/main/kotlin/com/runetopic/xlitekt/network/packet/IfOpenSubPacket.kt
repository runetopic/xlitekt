package com.runetopic.xlitekt.network.packet

/**
 * @author Tyler Telis
 */
data class IfOpenSubPacket(
    val interfaceId: Int,
    val toPackedInterface: Int,
    val isWalkable: Boolean
) : Packet