package xlitekt.game.packet

/**
 * @author Jordan Abraham
 *
 * <b>Information</b>
 * Represents the MAP_PROJANIM server -> client packet.
 *
 * This packet is used to add a mapProjAnim to a zone.
 *
 * <b>Assembly Example</b>
 * ```
 * writeByteAdd(packedOffset)
 * writeShortLittleEndian(-1)
 * writeByteNegate(distanceX)
 * writeShort(lifespan)
 * writeByte(startHeight)
 * writeShortLittleEndian(id)
 * writeByte(steepness)
 * writeByteNegate(angle)
 * writeShortLittleEndianAdd(delay)
 * writeByte(endHeight)
 * writeByteAdd(distanceZ)
 * ```
 *
 * @property id The id of the mapProjAnim. This is also the same as a normal spot animation.
 * @property distanceX The difference between the end location and start location on the X axis.
 * @property distanceZ The difference between the end location and start location on the Z axis.
 * @property startHeight The starting height.
 * @property endHeight The ending height.
 * @property delay The delay in ticks until the client will see this mapProjAnim.
 * @property lifespan The total lifespan in ticks the client will see this mapProjAnim.
 * @property angle The angle.
 * @property steepness The steepness.
 * @property packedOffset The packed offset location of the mapProjAnim relative to the client player location.
 */
data class MapProjAnimPacket(
    val id: Int,
    val distanceX: Int,
    val distanceZ: Int,
    val startHeight: Int,
    val endHeight: Int,
    val delay: Int,
    val lifespan: Int,
    val angle: Int,
    val steepness: Int,
    val packedOffset: Int
) : Packet
