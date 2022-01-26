package com.runetopic.xlitekt.network.packet

/**
 * @author Jordan Abraham
 */
data class IfOpenSubPacket(
    val interfaceId: Int,
    val hash: Int,
    val isWalkable: Boolean
) : Packet
