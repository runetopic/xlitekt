package xlitekt.game.world.map

import kotlinx.serialization.Serializable
import xlitekt.game.actor.movement.Direction
import xlitekt.game.actor.player.serializer.LocationSerializer
import xlitekt.game.world.map.zone.ZoneLocation

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
@Serializable(with = LocationSerializer::class)
@JvmInline
value class Location(
    val packedLocation: Int
) {
    constructor(x: Int, z: Int, level: Int = 0) : this((z and 0x3fff) or ((x and 0x3fff) shl 14) or ((level and 0x3) shl 28))

    inline val level get() = (packedLocation shr 28) and 0x3
    inline val x get() = (packedLocation shr 14) and 0x3fff
    inline val z: Int get() = packedLocation and 0x3fff
    inline val zoneX get() = (x shr 3)
    inline val zoneZ get() = (z shr 3)
    inline val zoneId get() = zoneX or (zoneZ shl 11) or (level shl 22)
    inline val regionX get() = (x shr 6)
    inline val regionZ get() = (z shr 6)
    inline val regionId get() = (regionX shl 8) or regionZ
    inline val regionLocation get() = z shr 13 or (x shr 13 shl 8) or (level shl 16)
    inline val zoneLocation get() = ZoneLocation(x shr 3, z shr 3, level)

    override fun toString(): String =
        "Location(packedCoordinates=$packedLocation, x=$x, z=$z, level=$level, zoneX=$zoneX, zoneZ=$zoneZ, zoneId=$zoneId, regionX=$regionX, regionZ=$regionZ, regionId=$regionId)"

    companion object {
        val None = Location(-1, -1, -1)
    }
}

fun Location.directionTo(end: Location): Direction = Direction(end.x - x, end.z - z)

fun Location.withinDistance(other: Location?, distance: Int = 15): Boolean {
    if (other == null) return false
    if (other.level != level) return false
    val deltaX = other.x - x
    val deltaZ = other.z - z
    return deltaX <= distance && deltaX >= -distance && deltaZ <= distance && deltaZ >= -distance
}

fun Location.localX(location: Location) = x - 8 * (location.zoneX - (104 shr 4))
fun Location.localZ(location: Location) = z - 8 * (location.zoneZ - (104 shr 4))

fun Location.transform(xOffset: Int, yOffset: Int, levelOffset: Int = 0) = Location(
    x = x + xOffset,
    z = z + yOffset,
    level = level + levelOffset
)
