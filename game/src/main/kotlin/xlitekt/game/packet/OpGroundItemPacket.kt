package xlitekt.game.packet

/**
 * @author Justin Kenney
 */
data class OpGroundItemPacket(
    val index: Int,
    val itemId: Int,
    val x: Int,
    val z: Int,
    val isModified: Boolean
) : Packet
