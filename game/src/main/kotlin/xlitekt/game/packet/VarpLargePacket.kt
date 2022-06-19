package xlitekt.game.packet

/**
 * @author Tyler Telis
 */
data class VarpLargePacket(
    val id: Int,
    val value: Int
) : Packet
