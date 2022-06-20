package xlitekt.game.packet

/**
 * @author Jordan Abraham
 *
 * <b>Information</b>
 *
 * Represents the LOC_ADD server -> client packet.
 *
 * This packet is used to add a loc to a zone.
 *
 * <b>Assembly Example</b>
 *
 * ```
 * writeByteSubtract((shape shl 2) or (rotation and 0x3))
 * writeByteSubtract(packedOffset)
 * writeShortLittleEndianAdd(id)
 * ```
 *
 * @property id The id of the loc.
 * @property shape The shape of the loc.
 * @property rotation The rotation of the loc.
 * @property packedOffset The packed offset location of the loc relative to the client player location.
 */
data class LocAddPacket(
    val id: Int,
    val shape: Int,
    val rotation: Int,
    val packedOffset: Int
) : Packet
