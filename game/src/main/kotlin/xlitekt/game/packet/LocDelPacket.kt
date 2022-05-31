package xlitekt.game.packet

/**
 * @author Jordan Abraham
 */
class LocDelPacket(
    val shape: Int,
    val rotation: Int,
    val packedOffset: Int
) : Packet
