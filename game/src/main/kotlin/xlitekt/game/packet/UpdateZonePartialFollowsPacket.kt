package xlitekt.game.packet

/**
 * @author Jordan Abraham
 *
 * Represents the UPDATE_ZONE_PARTIAL_FOLLOWS server -> client packet.
 *
 * This packet is used to prepare the client to update a single specific zone.
 *
 * The client takes an input X and Z location and sets temporary field values in code
 * which is next used by the following packets to update the zone.
 *
 * @see ObjAddPacket
 * @see ObjDelPacket
 * @see LocAddPacket
 * @see LocDelPacket
 * @see MapProjAnimPacket
 * @see UpdateZonePartialEnclosedPacket
 *
 * @property localX The X location of this zone relative to the location of the client player.
 * @property localZ The Z location of this zone relative to the location of the client player.
 */
data class UpdateZonePartialFollowsPacket(
    val localX: Int,
    val localZ: Int
) : Packet
