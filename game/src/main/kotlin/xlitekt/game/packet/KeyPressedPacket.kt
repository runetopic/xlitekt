package xlitekt.game.packet

/**
 * @author Justin Kenney
 */
data class KeyPressedPacket(
    val time: Long,
    val keycodes: Array<Int>
) : Packet
