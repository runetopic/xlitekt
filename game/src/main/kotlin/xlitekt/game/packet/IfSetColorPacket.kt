package xlitekt.game.packet

/**
 * @author Jordan Abraham
 */
data class IfSetColorPacket(
    val packedInterface: Int,
    val color: Int,
) : Packet
