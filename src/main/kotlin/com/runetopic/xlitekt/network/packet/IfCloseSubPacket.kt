package com.runetopic.xlitekt.network.packet

/**
 * @author Tyler Telis
 */
data class IfCloseSubPacket(
    val packedInterface: Int,
) : Packet
