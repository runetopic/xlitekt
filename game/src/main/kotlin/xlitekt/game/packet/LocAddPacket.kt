package xlitekt.game.packet

/**
 * @author Jordan Abraham
 */
class LocAddPacket(
    val id: Int,
    val shape: Int,
    val rotation: Int,
    val packedOffset: Int
) : Packet
