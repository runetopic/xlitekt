package xlitekt.game.packet

/**
 * @author Justin Kenney
 */
data class ExamineObjectPacket(
    val objectID: Int
) : Packet
