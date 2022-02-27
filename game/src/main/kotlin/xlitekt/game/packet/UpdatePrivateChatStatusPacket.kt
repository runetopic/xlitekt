package xlitekt.game.packet

/**
 * @author Tyler Telis
 */
data class UpdatePrivateChatStatusPacket(
    val mode: Int,
) : Packet
