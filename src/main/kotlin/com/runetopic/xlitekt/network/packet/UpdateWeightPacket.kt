package com.runetopic.xlitekt.network.packet

/**
 * @author Tyler Telis
 */
data class UpdateWeightPacket(
    val weight: Short,
) : Packet
