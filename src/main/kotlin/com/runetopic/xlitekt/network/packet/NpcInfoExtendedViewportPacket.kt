package com.runetopic.xlitekt.network.packet

import com.runetopic.xlitekt.game.actor.player.Player

/**
 * @author Tyler Telis
 */
data class NpcInfoExtendedViewportPacket(override val player: Player) : NpcInfoPacket(player)
