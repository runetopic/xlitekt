package xlitekt.game.world.map.zone

import xlitekt.game.world.map.Location

/**
 * @author Tyler Telis
 * @author Jordan Abraham
 */
class Zones {
    private val zones = arrayOfNulls<Zone?>(2048 * 2048 * 4)

    operator fun get(location: Location): Zone {
        val zoneLocation = location.zoneLocation
        return zones[zoneLocation.packedLocation] ?: createZone(zoneLocation)
    }

    fun createZone(location: ZoneLocation): Zone {
        val currentZone = zones[location.packedLocation]
        if (currentZone != null) return currentZone
        val newZone = Zone(location.location)
        zones[location.packedLocation] = newZone
        return newZone
    }

    internal fun filterNotNull() = zones.filterNotNull()
}
