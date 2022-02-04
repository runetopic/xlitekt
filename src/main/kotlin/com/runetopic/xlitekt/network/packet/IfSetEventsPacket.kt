package com.runetopic.xlitekt.network.packet

/**
 * @author Tyler Telis
 */
data class IfSetEventsPacket(
    val interfaceId: Int,
    var childId: Int,
    val fromSlot: Int,
    val toSlot: Int,
    val events: Int
) : Packet
