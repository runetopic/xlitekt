package xlitekt.game.packet

/**
 * @author Justin Kenney
 */
data class FocusChangePacket(
    val isFocused: Boolean
) : Packet
