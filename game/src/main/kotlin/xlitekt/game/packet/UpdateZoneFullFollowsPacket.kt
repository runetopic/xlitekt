package xlitekt.game.packet

/**
 * @author Jordan Abraham
 */
data class UpdateZoneFullFollowsPacket(
    val localX: Int,
    val localZ: Int
) : Packet
