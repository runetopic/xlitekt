package com.runetopic.xlitekt.network.packet

data class DisplayModePacket(
    val displayMode: Int,
    val width: Int,
    val height: Int
) : Packet
