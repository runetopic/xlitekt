package xlitekt.game.packet

/**
 * @author Tyler Telis
 */
data class UpdateRebootTimerPacket(
    val rebootTimer: Int,
) : Packet
