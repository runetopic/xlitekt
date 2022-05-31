package xlitekt.game.packet

/**
 * @author Jordan Abraham
 */
data class UpdateZonePartialEnclosedPacket(
    val localX: Int,
    val localZ: Int,
    val bytes: ByteArray
) : Packet
