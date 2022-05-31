package xlitekt.game.world.map.zone

import xlitekt.game.world.map.location.Location
import xlitekt.game.world.map.location.ZoneLocation

private const val ZONES = 2048 * 2048 * 4

class Zones {
    private val zones: Array<Zone?> = arrayOfNulls(ZONES)

    operator fun get(location: Location): Zone {
        val zoneLocation = location.toZoneLocation()
        return zones[zoneLocation.packedCoordinates] ?: createZone(zoneLocation)
    }

    fun createZone(location: ZoneLocation): Zone {
        val currentZone = zones[location.packedCoordinates]
        if (currentZone != null) return currentZone
        val newZone = Zone(location.toFullLocation())
        zones[location.packedCoordinates] = newZone
        return newZone
    }
}
