package xlitekt.game.packet

/**
 * @author Tyler Telis
 */
data class SetPlayerOpPacket(
    val priority: Boolean,
    val option: String,
    val index: Int,
) : Packet
