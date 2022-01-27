package com.runetopic.xlitekt.network.packet

/**
 * @author Tyler Telis
 */
data class IfSetHiddenPacket(
    val packedInterface: Int,
    val hidden: Boolean,
) : Packet
