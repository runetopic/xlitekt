package xlitekt.game.packet

/**
 * @author Jordan Abraham
 *
 * Represents the UPDATE_ZONE_PARTIAL_FOLLOWS server -> client packet.
 * This packet is used to prepare the client to update a single specific zone.
 * The client takes an input X and Z location and sets temporary field values in code
 * which is next used by the following packets to update the zone.
 *
 * @see ObjAddPacket To add a obj to a zone.
 * @see ObjDelPacket To delete a obj from a zone.
 * @see LocAddPacket To add a loc to a zone.
 * @see LocDelPacket To delete a loc from a zone.
 * @see MapProjAnimPacket To add a mapProjAnim to a zone.
 *
 * @see UpdateZonePartialEnclosedPacket To send multiple updates to a zone instead of a single update.
 *
 * @property localX The X location of this zone relative to the location of the client player.
 * @property localZ The Z location of this zone relative to the location of the client player.
 */
data class UpdateZonePartialFollowsPacket(
    val localX: Int,
    val localZ: Int
) : Packet
