package xlitekt.game.packet

/**
 * @author Jordan Abraham
 */
class MapProjAnimPacket(
    val id: Int,
    val distanceX: Int,
    val distanceZ: Int,
    val startHeight: Int,
    val endHeight: Int,
    val delay: Int,
    val lifespan: Int,
    val angle: Int,
    val steepness: Int,
    val packedOffset: Int
) : Packet
