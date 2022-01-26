package com.runetopic.xlitekt.network.packet

/**
 * @author Tyler Telis
 */
data class IfOpenSubPacket(
    val interfaceId: Int,
    val hash: Int,
    val isWalkable: Boolean
) : Packet
