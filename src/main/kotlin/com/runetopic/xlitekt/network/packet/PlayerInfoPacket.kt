package com.runetopic.xlitekt.network.packet

import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.game.actor.render.Render

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
data class PlayerInfoPacket(
    val player: Player,
    val updates: List<Render>
) : Packet
