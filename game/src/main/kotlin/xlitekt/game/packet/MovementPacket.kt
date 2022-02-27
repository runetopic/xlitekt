package xlitekt.game.packet

data class MovementPacket(
    val destinationX: Int,
    val destinationZ: Int,
    val movementType: Int
) : Packet
