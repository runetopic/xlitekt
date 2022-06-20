package xlitekt.game.packet

/**
 * @author Jordan Abraham
 *
 * <b>Information</b>
 *
 * Represents the IF_OPENTOP server -> client packet.
 *
 * This packet is used to open a top level interface.
 *
 * <b>Assembly Example</b>
 *
 * ```
 * writeShort(interfaceId)
 * ```
 */
data class IfOpenTopPacket(
    val interfaceId: Int
) : Packet
