package xlitekt.game.packet

import xlitekt.game.content.friend.Friend

/**
 * @author Tyler Telis
 */
data class UpdateFriendListPacket(
    val friends: List<Friend>,
) : Packet
