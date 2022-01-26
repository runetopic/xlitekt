package com.runetopic.xlitekt.network.packet

/**
 * @author Tyler Telis
 */
data class SetMapFlagPacket(
    val destinationX: Int,
    val destinationZ: Int,
) : Packet
