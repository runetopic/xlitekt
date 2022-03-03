package xlitekt.game.packet

/**
 * @author Tyler Telis
 */
data class IfCloseSubPacket(
    val packedInterface: Int,
) : Packet
