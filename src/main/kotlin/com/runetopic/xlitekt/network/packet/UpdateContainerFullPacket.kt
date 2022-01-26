package com.runetopic.xlitekt.network.packet

import com.runetopic.xlitekt.game.item.Item

/**
 * @author Tyler Telis
 */
data class UpdateContainerFullPacket(
    val packedInterface: Int,
    val containerKey: Int,
    val items: List<Item?>
) : Packet
