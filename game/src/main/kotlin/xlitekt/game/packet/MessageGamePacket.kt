package xlitekt.game.packet

/**
 * @author Tyler Telis
 */
data class MessageGamePacket(
    val type: Int,
    val message: String,
    val hasPrefix: Boolean = false,
    val prefix: String = ""
) : Packet
