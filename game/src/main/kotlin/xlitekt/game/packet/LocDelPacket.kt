package xlitekt.game.packet

/**
 * <b>Information</b>
 *
 * Represents the LOC_DEL server -> client packet.
 *
 * This packet is used to delete a loc from a zone.
 *
 * <b>Assembly Example</b>
 *
 * ```
 * writeByte((shape shl 2) or (rotation and 0x3))
 * writeByteAdd(packedOffset)
 * ```
 *
 * @author Jordan Abraham
 *
 * @see ObjAddPacket
 * @see ObjDelPacket
 * @see LocAddPacket
 * @see MapProjAnimPacket
 * @see UpdateZoneFullFollowsPacket
 * @see UpdateZonePartialEnclosedPacket
 * @see UpdateZonePartialFollowsPacket
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
