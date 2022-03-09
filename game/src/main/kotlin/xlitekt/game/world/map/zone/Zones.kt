package xlitekt.game.world.map.zone

import java.util.Collections
import xlitekt.game.world.map.location.Location

class Zone

object Zones {
    val zones: MutableMap<Location, Zone> = Collections.synchronizedMap(mutableMapOf())

    fun getOrCreate(location: Location): Zone {
        val current = zones[location]
        if (current != null) return current
        val zone = Zone()
        zones[location] = zone
        return zone
    }
}
