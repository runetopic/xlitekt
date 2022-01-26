package com.runetopic.xlitekt.network.packet

/**
 * @author Tyler Telis
 */
data class UpdatePrivateChatStatusPacket(
    val mode: Int,
) : Packet
