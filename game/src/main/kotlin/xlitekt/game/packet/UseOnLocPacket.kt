package xlitekt.game.packet

/**
 * @author Justin Kenney
 */
data class UseOnLocPacket(
    val itemId: Int,
    val locId: Int,
    val x: Int,
    val z: Int,
    val slotId: Int,
    val isModified: Boolean,
    val packedInterface: Int
) : Packet
