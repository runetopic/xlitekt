package com.runetopic.xlitekt.network.packet

import com.runetopic.xlitekt.game.actor.player.friends.Friend

/**
 * @author Tyler Telis
 */
data class UpdateRebootTimerPacket(
    val rebootTimer: Int,
) : Packet
