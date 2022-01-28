package com.runetopic.xlitekt.network.packet

import com.runetopic.xlitekt.game.actor.player.Player

/**
 * @author Tyler Telis
 */
open class NpcInfoPacket(
    open val player: Player
) : Packet
