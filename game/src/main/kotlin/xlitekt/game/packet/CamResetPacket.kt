package xlitekt.game.packet

/**
 * @author Jordan Abraham
 *
 * <b>Information</b>
 *
 * Represents the CAM_RESET server -> client packet.
 *
 * This packet is used to reset the client camera position to the default position.
 *
 * <b>Assembly Example</b>
 *
 * This packet has an empty body and does not require additional information to send to the client.
 */
class CamResetPacket : Packet
