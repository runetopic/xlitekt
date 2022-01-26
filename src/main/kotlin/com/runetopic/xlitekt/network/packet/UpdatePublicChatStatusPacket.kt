package com.runetopic.xlitekt.network.packet

/**
 * @author Tyler Telis
 */
data class UpdatePublicChatStatusPacket(
    val tradeMode: Int,
    val chatMode: Int
) : Packet
