package xlitekt.game.packet

/**
 * @author Tyler Telis
 */
data class SetMapFlagPacket(
    val destinationX: Int,
    val destinationZ: Int,
) : Packet
