package com.runetopic.xlitekt.network.packet

import com.runetopic.xlitekt.game.actor.player.Player

/**
 * @author Tyler Telis
 */
data class NPCInfoExtendedViewportPacket(override val player: Player) : NPCInfoPacket(player)
