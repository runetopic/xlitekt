package com.runetopic.xlitekt.network.packet

data class WindowStatusPacket(
    val displayMode: Int,
    val width: Int,
    val height: Int
) : Packet
