package xlitekt.game.packet

/**
 * @author Tyler Telis
 *
 * <b>Information</b>
 *
 * Represents the IF_MOVESUB server -> client packet.
 *
 * This packet is used to move a sub level interface from one position to another.
 *
 * <b>Assembly Example</b>
 *
 * ```
 * writeInt(fromPackedInterface)
 * writeInt(toPackedInterface)
 * ```
 *
 * @property fromPackedInterface The packed interface to move from (interfaceId shl 16 or childId).
 * @property toPackedInterface The packed interface to move to (interfaceId shl 16 or childId).
 */
data class IfMoveSubPacket(
    val fromPackedInterface: Int,
    val toPackedInterface: Int,
) : Packet
