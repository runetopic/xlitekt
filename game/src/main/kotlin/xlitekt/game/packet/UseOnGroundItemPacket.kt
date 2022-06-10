package xlitekt.game.packet

/**
 * @author Justin Kenney
 */
data class UseOnGroundItemPacket(
    val itemId: Int,
    val slotId: Int,
    val groundItemId: Int,
    val x: Int,
    val z: Int,
    val packedInterface: Int,
    val isModified: Boolean
) : Packet
