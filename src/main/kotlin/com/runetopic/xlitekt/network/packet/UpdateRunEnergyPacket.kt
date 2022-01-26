package com.runetopic.xlitekt.network.packet

/**
 * @author Tyler Telis
 */
data class UpdateRunEnergyPacket(
    val energy: Float,
) : Packet
