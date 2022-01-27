package com.runetopic.xlitekt.network.packet

import com.runetopic.xlitekt.game.actor.player.friends.Friend

/**
 * @author Tyler Telis
 */
data class UpdateFriendListPacket(
    val friends: List<Friend>,
) : Packet
