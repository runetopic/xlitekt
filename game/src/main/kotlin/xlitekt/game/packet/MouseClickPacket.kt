package xlitekt.game.packet

/**
 * @author Justin Kenney
 */
data class MouseClickPacket(
    val time: Long,
    val x: Int,
    val y: Int
) : Packet
