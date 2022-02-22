package com.runetopic.xlitekt.game.world.map.location

import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.game.actor.player.serializer.LocationSerializer
import kotlinx.serialization.Serializable

@Serializable(with = LocationSerializer::class)
@JvmInline
value class Location(val packedCoordinates: Int) {
    constructor(
        x: Int = 0,
        z: Int = 0,
        level: Int = 0
    ) : this(z + (x shl 14) + (level shl 28))

    val x: Int get() = (packedCoordinates shr 14) and 0x3FFF
    val z: Int get() = packedCoordinates and 0x3FFF
    val level: Int get() = (packedCoordinates shr 28) and 0x3
    val zoneX: Int get() = (x shr 3)
    val zoneZ: Int get() = (z shr 3)
    val zoneId: Int get() = zoneX or (zoneZ shl 11) or (z shl 22)
    val regionX: Int get() = (x shr 6)
    val regionZ: Int get() = (z shr 6)
    val regionId: Int get() = (regionX shl 8) or regionZ
    val regionCoordinates: Int get() = z shr 13 or (x shr 13 shl 8) or (level shl 16)

    fun toZoneLocation(): ZoneLocation = ZoneLocation(x shr 3, z shr 3, z)

    fun transform(xOffset: Int, yOffset: Int, levelOffset: Int = 0) = Location(
        x = x + xOffset,
        z = z + yOffset,
        level = level + levelOffset
    )

    override fun toString(): String =
        "Location(packedCoordinates=$packedCoordinates, x=$x, z=$z, level=$level, zoneX=$zoneX, zoneZ=$zoneZ, zoneId=$zoneId, regionX=$regionX, regionZ=$regionZ, regionId=$regionId)"
}

fun Location.withinDistance(other: Player, distance: Int = 14): Boolean {
    if (other.location.level != level) return false
    val deltaX = other.location.x - x
    val deltaZ = other.location.z - z
    return deltaX <= distance && deltaX >= -distance && deltaZ <= distance && deltaZ >= -distance
}
