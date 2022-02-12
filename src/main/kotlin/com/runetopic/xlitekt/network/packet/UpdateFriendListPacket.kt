package com.runetopic.xlitekt.network.packet

import com.runetopic.xlitekt.game.social.friend.Friend

/**
 * @author Tyler Telis
 */
data class UpdateFriendListPacket(
    val friends: List<Friend>,
) : Packet
