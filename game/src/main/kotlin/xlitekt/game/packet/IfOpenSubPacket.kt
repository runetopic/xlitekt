package xlitekt.game.packet

/**
 * @author Tyler Telis
 *
 * <b>Information</b>
 *
 * Represents the IF_OPENSUB server -> client packet.
 *
 * This packet is used to open a sub level interface. A sub level interface is that of which
 * is put onto a top level interface. An example of this is displaying the chat box interface
 * onto the fixed game mode top level interface.
 *
 * <b>Assembly Example</b>
 *
 * ```
 * writeShortLittleEndian(interfaceId)
 * writeByteSubtract(if (walkable) 1 else 0)
 * writeInt(toPackedInterface)
 * ```
 *
 * @property interfaceId The interface ID to use for the sub level interface.
 * @property toPackedInterface The top level packed interface to put this sub interface onto.
 * @property walkable True if this interface stays open while the client player is walking.
 */
data class IfOpenSubPacket(
    val interfaceId: Int,
    val toPackedInterface: Int,
    val walkable: Boolean
) : Packet
