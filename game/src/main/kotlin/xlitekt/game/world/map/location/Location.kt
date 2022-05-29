package xlitekt.game.world.map.location

import kotlinx.serialization.Serializable
import xlitekt.game.actor.movement.Direction
import xlitekt.game.actor.player.serializer.LocationSerializer

@Serializable(with = LocationSerializer::class)
@JvmInline
value class Location(val packedLocation: Int) {
    constructor(x: Int, z: Int, level: Int = 0) : this((z and 0x3FFF) or ((x and 0x3FFF) shl 14) or ((level and 0x3) shl 28))

    val x: Int get() = (packedLocation shr 14) and 0x3FFF
    val z: Int get() = packedLocation and 0x3FFF
    val level: Int get() = (packedLocation shr 28) and 0x3
    val zoneX: Int get() = (x shr 3)
    val zoneZ: Int get() = (z shr 3)
    val zoneId: Int get() = zoneX or (zoneZ shl 11) or (level shl 22)
    val regionX: Int get() = (x shr 6)
    val regionZ: Int get() = (z shr 6)
    val regionId: Int get() = (regionX shl 8) or regionZ
    val regionLocation: Int get() = z shr 13 or (x shr 13 shl 8) or (level shl 16)

    fun localX(location: Location) = x - 8 * (location.zoneX - (104 shr 4))
    fun localZ(location: Location) = z - 8 * (location.zoneZ - (104 shr 4))
    fun toZoneLocation(): ZoneLocation = ZoneLocation(x shr 3, z shr 3, level)

    fun transform(xOffset: Int, yOffset: Int, levelOffset: Int = 0) = Location(
        x = x + xOffset,
        z = z + yOffset,
        level = level + levelOffset
    )

    override fun toString(): String =
        "Location(packedCoordinates=$packedLocation, x=$x, z=$z, level=$level, zoneX=$zoneX, zoneZ=$zoneZ, zoneId=$zoneId, regionX=$regionX, regionZ=$regionZ, regionId=$regionId)"
}

fun Location.directionTo(end: Location): Direction = Direction.directionFromDelta(end.x - x, end.z - z)

fun Location.withinDistance(other: Location?, distance: Int = 14): Boolean {
    if (other == null) return false
    if (other.level != level) return false
    val deltaX = other.x - x
    val deltaZ = other.z - z
    return deltaX <= distance && deltaX >= -distance && deltaZ <= distance && deltaZ >= -distance
}
