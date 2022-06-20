package xlitekt.game.packet

/**
 * <b>Information</b>
 *
 * Represents the OBJ_DEL server -> client packet.
 *
 * This packet is used to delete an obj from a zone.
 *
 * <b>Assembly Example</b>
 *
 * ```
 * writeByteAdd(packedOffset)
 * writeShortLittleEndian(id)
 * ```
 *
 * @author Jordan Abraham
 *
 * @see ObjAddPacket
 * @see LocAddPacket
 * @see LocDelPacket
 * @see MapProjAnimPacket
 * @see UpdateZoneFullFollowsPacket
 * @see UpdateZonePartialEnclosedPacket
 * @see UpdateZonePartialFollowsPacket
 *
 * @property id The id of the obj.
 * @property packedOffset The packed offset location of the obj relative to the client player location.
 */
data class ObjDelPacket(
    val id: Int,
    val packedOffset: Int
) : Packet
