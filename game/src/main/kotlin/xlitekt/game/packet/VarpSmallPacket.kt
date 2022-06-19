package xlitekt.game.packet

/**
 * @author Tyler Telis
 */
data class VarpSmallPacket(
    val id: Int,
    val value: Int
) : Packet
