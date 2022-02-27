package com.runetopic.xlitekt.network.packet

import com.runetopic.xlitekt.game.actor.player.Viewport

/**
 * @author Tyler Telis
 */
data class NPCInfoExtendedPacket(
    val viewport: Viewport
) : Packet
