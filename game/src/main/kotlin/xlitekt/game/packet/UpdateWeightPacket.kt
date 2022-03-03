package xlitekt.game.packet

/**
 * @author Tyler Telis
 */
data class UpdateWeightPacket(
    val weight: Short,
) : Packet
