package xlitekt.game.packet

/**
 * @author Jordan Abraham
 *
 * <b>Information</b>
 *
 * Represents the IF_OPENTOP server -> client packet.
 *
 * This packet is used to open a top level interface. A top level interface is that of which
 * is dependent on the client resizeable mode. The top level interface is used as a base to
 * put sub level interfaces onto.
 *
 * <b>Assembly Example</b>
 *
 * ```
 * writeShort(interfaceId)
 * ```
 *
 * @property interfaceId The interface ID to use for the top level interface.
 */
data class IfOpenTopPacket(
    val interfaceId: Int
) : Packet
