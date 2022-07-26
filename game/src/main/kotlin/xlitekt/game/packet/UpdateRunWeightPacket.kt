package xlitekt.game.packet

/**
 * <b>Information</b>
 *
 * Represents the UPDATE_RUNWEIGHT server -> client packet.
 *
 * This packet is used to update the client on what the current weight of the player is.
 *
 * <b>Assembly Example</b>
 *
 * ```
 * writeShort(weight.toInt())
 * ```
 *
 * @author Tyler Telis
 *
 * @property weight The weight of the player.
 */
data class UpdateRunWeightPacket(
    val weight: Float,
) : Packet
