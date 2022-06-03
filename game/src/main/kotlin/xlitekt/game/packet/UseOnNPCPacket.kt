package xlitekt.game.packet

/**
 * @author Justin Kenney
 */
data class UseOnNPCPacket(
    val itemId: Int,
    val npcIndex: Int,
    val slotId: Int,
    val isModified: Boolean,
    val interfaceId: Int
) : Packet
