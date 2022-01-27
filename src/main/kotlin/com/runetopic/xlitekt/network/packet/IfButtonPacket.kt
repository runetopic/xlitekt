package com.runetopic.xlitekt.network.packet

/**
 * @author Jordan Abraham
 */
data class IfButtonPacket(
    val index: Int,
    val packedInterface: Int,
    val slotId: Int,
    val itemId: Int
) : Packet
