package xlitekt.game.packet

/**
 * @author Justin Kenney
 */
data class UseOnInventoryItemPacket(
    val fromItemId: Int,
    val fromSlotId: Int,
    val fromPackedInterface: Int,
    val toItemId: Int,
    val toSlotId: Int,
    val toPackedInterface: Int

) : Packet
