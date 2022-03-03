package xlitekt.game.packet

/**
 * @author Tyler Telis
 */
data class IfOpenSubPacket(
    val interfaceId: Int,
    val toPackedInterface: Int,
    val alwaysOpen: Boolean
) : Packet
