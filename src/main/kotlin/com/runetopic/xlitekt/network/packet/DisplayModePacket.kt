package com.runetopic.xlitekt.network.packet

data class DisplayModePacket(
    val mode: Int,
    val width: Int,
    val height: Int
) : Packet
