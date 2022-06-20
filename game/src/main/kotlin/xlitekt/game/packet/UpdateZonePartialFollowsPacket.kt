package xlitekt.game.packet

/**
 * <b>Information</b>
 *
 * Represents the UPDATE_ZONE_PARTIAL_FOLLOWS server -> client packet.
 *
 * This packet is used to prepare the client to update a single specific zone.
 *
 * The client takes an input X and Z location and sets temporary field values in code
 * which is next used by the following packets to update the zone.
 *
 * <b>Assembly Example</b>
 *
 * ```
 * writeByte((localZ shr 3) shl 3)
 * writeByteSubtract((localX shr 3) shl 3)
 * ```
 *
 * @author Jordan Abraham
 *
 * @see ObjAddPacket
 * @see ObjDelPacket
 * @see LocAddPacket
 * @see LocDelPacket
 * @see MapProjAnimPacket
 * @see UpdateZoneFullFollowsPacket
 * @see UpdateZonePartialEnclosedPacket
 *
 * @property localX The X location of this zone relative to the location of the client player.
 * @property localZ The Z location of this zone relative to the location of the client player.
 */
data class UpdateZonePartialFollowsPacket(
    val localX: Int,
    val localZ: Int
) : Packet
