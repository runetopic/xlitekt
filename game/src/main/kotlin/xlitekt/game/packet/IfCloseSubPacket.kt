package xlitekt.game.packet

/**
 * <b>Information</b>
 *
 * Represents the IF_CLOSESUB server -> client packet.
 *
 * This packet is used to close a sub level interface. A sub level interface is that of which
 * is contained in top level interface. An example of this is the chat box interface
 * on the fixed game mode top level interface.
 *
 * <b>Assembly Example</b>
 *
 * ```
 * writeInt(packedInterface)
 * ```
 *
 * @author Tyler Telis
 *
 * @property packedInterface The packed interface that is to be closed client sided (interfaceId shl 16 or childId).
 */
data class IfCloseSubPacket(
    val packedInterface: Int,
) : Packet
