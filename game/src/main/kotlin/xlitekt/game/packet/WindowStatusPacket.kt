package xlitekt.game.packet

data class WindowStatusPacket(
    val displayMode: Int,
    val width: Int,
    val height: Int
) : Packet
