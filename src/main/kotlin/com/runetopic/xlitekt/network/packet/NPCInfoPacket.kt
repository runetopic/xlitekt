package com.runetopic.xlitekt.network.packet

import com.runetopic.xlitekt.game.actor.player.Viewport

/**
 * @author Jordan Abraham
 */
data class NPCInfoPacket(
    val viewport: Viewport
) : Packet
