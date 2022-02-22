package com.runetopic.xlitekt.network.packet

import com.runetopic.xlitekt.game.actor.player.Viewport
import com.runetopic.xlitekt.game.world.map.location.Location

/**
 * @author Jordan Abraham
 */
data class RebuildNormalPacket(
    val viewport: Viewport,
    val location: Location,
    val update: Boolean
) : Packet
