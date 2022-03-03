package xlitekt.game.packet

/**
 * @author Tyler Telis
 */
data class UpdateStatPacket(
    val xp: Double,
    val level: Int,
    val skillId: Int,
) : Packet
