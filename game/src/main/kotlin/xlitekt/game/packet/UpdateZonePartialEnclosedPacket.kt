package xlitekt.game.packet

/**
 * @author Jordan Abraham
 *
 * <b>Information</b>
 *
 * Represents the UPDATE_ZONE_PARTIAL_ENCLOSED server -> client packet.
 *
 * This packet is used to send multiple updates together to a single specific zone.
 *
 * The client takes an input X and Z location and loops through the rest of the packet
 * bytes to send multiple updates to a single zone.
 *
 * <b>Assembly Example</b>
 *
 * ```
 * writeByteNegate((localZ shr 3) shl 3)
 * writeByte((localX shr 3) shl 3)
 * writeBytes(bytes)
 * ```
 *
 * @see ObjAddPacket
 * @see ObjDelPacket
 * @see LocAddPacket
 * @see LocDelPacket
 * @see MapProjAnimPacket
 * @see UpdateZonePartialFollowsPacket
 *
 * @property localX The X location of this zone relative to the location of the client player.
 * @property localZ The Z location of this zone relative to the location of the client player.
 * @property bytes The [ByteArray] of updates to send to the client for updates to a zone.
 */
data class UpdateZonePartialEnclosedPacket(
    val localX: Int,
    val localZ: Int,
    val bytes: ByteArray
) : Packet {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UpdateZonePartialEnclosedPacket

        if (localX != other.localX) return false
        if (localZ != other.localZ) return false
        if (!bytes.contentEquals(other.bytes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = localX
        result = 31 * result + localZ
        result = 31 * result + bytes.contentHashCode()
        return result
    }
}
