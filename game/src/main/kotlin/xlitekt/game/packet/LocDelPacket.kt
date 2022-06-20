package xlitekt.game.packet

/**
 * @author Jordan Abraham
 *
 * Represents the LOC_DEL server -> client packet.
 *
 * This packet is used to delete a loc from a zone.
 *
 * @property shape The shape of the loc.
 * @property rotation The rotation of the loc.
 * @property packedOffset The packed offset location of the loc relative to the client player location.
 */
data class LocDelPacket(
    val shape: Int,
    val rotation: Int,
    val packedOffset: Int
) : Packet
