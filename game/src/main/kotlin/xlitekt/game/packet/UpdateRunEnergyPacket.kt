package xlitekt.game.packet

/**
 * @author Tyler Telis
 */
data class UpdateRunEnergyPacket(
    val energy: Float,
) : Packet
