package xlitekt.game.packet

import xlitekt.game.friend.Friend

/**
 * @author Tyler Telis
 */
data class UpdateFriendListPacket(
    val friends: List<Friend>,
) : Packet
