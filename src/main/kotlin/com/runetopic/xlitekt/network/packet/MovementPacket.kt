package com.runetopic.xlitekt.network.packet

data class MovementPacket(
    val x: Int,
    val z: Int,
    val movementType: Int
) : Packet
