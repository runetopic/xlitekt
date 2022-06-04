package xlitekt.game.packet

data class MovementPacket(
    val destinationX: Int,
    val destinationZ: Int,
    val isModified: Boolean
) : Packet
