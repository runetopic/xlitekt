package xlitekt.game.packet

/**
 * @author Tyler Telis
 */
data class UpdateStatPacket(
    val skillId: Int,
    val level: Int,
    val xp: Double,
) : Packet
