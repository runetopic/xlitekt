package xlitekt.game.packet

/**
 * @author Jordan Abraham
 */
data class IfButtonPacket(
    val index: Int,
    val packedInterface: Int,
    val slotId: Int,
    val itemId: Int
) : Packet
