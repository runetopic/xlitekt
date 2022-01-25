package com.runetopic.xlitekt.network.packet

import com.runetopic.xlitekt.game.actor.player.Player

/**
 * @author Jordan Abraham
 */
data class PlayerInfoPacket(
    val player: Player
) : Packet
