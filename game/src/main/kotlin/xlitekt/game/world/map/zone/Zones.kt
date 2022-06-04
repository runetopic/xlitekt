package xlitekt.game.world.map.zone

import xlitekt.game.world.map.Location

/**
 * @author Tyler Telis
 * @author Jordan Abraham
 */
class Zones {
    private val zones = arrayOfNulls<Zone?>(2048 * 2048 * 4)

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

    internal fun filterNotNull() = zones.filterNotNull()
}
