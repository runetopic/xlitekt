package xlitekt.game.packet

/**
 * @author Jordan Abraham
 */
data class UpdateZonePartialFollowsPacket(
    val localX: Int,
    val localZ: Int
) : Packet
