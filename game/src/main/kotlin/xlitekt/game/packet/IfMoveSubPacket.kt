package xlitekt.game.packet

/**
 * @author Tyler Telis
 */
data class IfMoveSubPacket(
    val fromPackedInterface: Int,
    val toPackedInterface: Int,
) : Packet
