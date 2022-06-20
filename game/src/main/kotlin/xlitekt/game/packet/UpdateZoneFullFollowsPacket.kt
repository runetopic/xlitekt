package xlitekt.game.packet

/**
 * @author Jordan Abraham
 *
 * <b>Information</b>
 * Represents the UPDATE_ZONE_FULL_FOLLOWS server -> client packet.
 *
 * This packet is used to clear a specific zone of updates.
 *
 * The client takes an input X and Z location and loops +8 on both axis to clear
 * the entire zone of locs and objs.
 *
 * ```
 * for (x in localX + 8) {
 *      for (z in localZ + 8) {
 *          // Loops the 8x8 zone and clears each tile of locs and objs.
 *      }
 * }
 * ```
 *
 * <b>Assembly Example</b>
 * ```
 * writeByteAdd((localX shr 3) shl 3)
 * writeByte((localZ shr 3) shl 3)
 * ```
 *
 * @see UpdateZonePartialFollowsPacket
 * @see UpdateZonePartialEnclosedPacket
 *
 * @property localX The X location of this zone relative to the location of the client player.
 * @property localZ The Z location of this zone relative to the location of the client player.
 */
data class UpdateZoneFullFollowsPacket(
    val localX: Int,
    val localZ: Int
) : Packet
