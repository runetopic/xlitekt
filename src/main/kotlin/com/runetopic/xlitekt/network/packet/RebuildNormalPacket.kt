package com.runetopic.xlitekt.network.packet

import com.runetopic.xlitekt.game.actor.player.Viewport
import com.runetopic.xlitekt.game.tile.Tile

/**
 * @author Jordan Abraham
 */
data class RebuildNormalPacket(
    val viewport: Viewport,
    val tile: Tile,
    val update: Boolean
) : Packet
