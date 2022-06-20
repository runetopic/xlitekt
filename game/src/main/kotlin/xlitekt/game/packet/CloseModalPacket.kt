package xlitekt.game.packet

/**
 * @author Jordan Abraham
 *
 * <b>Information</b>
 *
 * Represents the CLOSE_MODAL client -> server packet.
 *
 * This packet is used to handle when the client closes a modal. The modal is the
 * interface that is open in the center of the client that is heavily interacted with.
 *
 * <b>Disassembly Exmaple</b>
 *
 * This packet has an empty body.
 */
class CloseModalPacket : Packet
