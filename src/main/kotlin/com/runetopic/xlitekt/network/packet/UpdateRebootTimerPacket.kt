package com.runetopic.xlitekt.network.packet

/**
 * @author Tyler Telis
 */
data class UpdateRebootTimerPacket(
    val rebootTimer: Int,
) : Packet
