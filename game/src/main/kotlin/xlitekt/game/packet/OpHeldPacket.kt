package xlitekt.game.packet

/**
 * @author Jordan Abraham
 */
data class OpHeldPacket(
    val index: Int,
    val fromPackedInterface: Int,
    val fromSlotId: Int,
    val fromItemId: Int,
    val toPackedInterface: Int,
    val toSlotId: Int,
    val toItemId: Int
) : Packet
