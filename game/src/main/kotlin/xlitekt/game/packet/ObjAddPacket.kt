package xlitekt.game.packet

/**
 * @author Jordan Abraham
 */
class ObjAddPacket(
    val id: Int,
    val amount: Int,
    val packedOffset: Int
) : Packet
