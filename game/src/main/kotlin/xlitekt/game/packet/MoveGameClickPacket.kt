package xlitekt.game.packet

data class MoveGameClickPacket(
    val destinationX: Int,
    val destinationZ: Int,
    val movementType: Int
) : Packet
