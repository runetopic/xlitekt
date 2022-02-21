package com.runetopic.xlitekt.game.zone

import com.runetopic.xlitekt.game.location.Location
import com.runetopic.xlitekt.game.location.ZoneLocation

/**
 * @author Kris | 09/02/2022
 */
@Suppress("NOTHING_TO_INLINE")
object ZoneFlags {
    private const val ZONES = 2048 * 2048 * 4
    const val ZONE_SIZE = 8 * 8
    val flags: Array<IntArray?> = arrayOfNulls(ZONES)

    inline fun alloc(zoneLocation: ZoneLocation): IntArray {
        val packed = zoneLocation.packedCoordinates
        val current = flags[packed]
        if (current != null) return current
        val new = IntArray(ZONE_SIZE)
        flags[packed] = new
        return new
    }

    inline fun destroy(zoneLocation: ZoneLocation) {
        flags[zoneLocation.packedCoordinates] = null
    }

    inline operator fun set(location: Location, flag: Int) = set(location.x, location.z, location.z, flag)

    inline operator fun set(x: Int, y: Int, z: Int, flag: Int) {
        alloc(ZoneLocation(x shr 3, y shr 3, z))[zoneLocal(x, y)] = flag
    }

    inline fun add(location: Location, flag: Int) = add(location.x, location.z, location.z, flag)

    inline fun add(x: Int, y: Int, z: Int, flag: Int) {
        val flags = alloc(ZoneLocation(x shr 3, y shr 3, z))
        val index = zoneLocal(x, y)
        val cur = flags[index]
        flags[index] = cur or flag
    }

    inline fun remove(location: Location, flag: Int) = remove(location.x, location.z, location.z, flag)

    inline fun remove(x: Int, y: Int, z: Int, flag: Int) {
        val flags = alloc(ZoneLocation(x shr 3, y shr 3, z))
        val index = zoneLocal(x, y)
        val cur = flags[index]
        flags[index] = cur and flag.inv()
    }

    inline operator fun get(location: Location): Int = get(location.x, location.z, location.z)

    inline operator fun get(x: Int, y: Int, z: Int, default: Int = 0): Int {
        val zoneLocation = ZoneLocation(x shr 3, y shr 3, z)
        val array = flags[zoneLocation.packedCoordinates] ?: return default
        return array[zoneLocal(x, y)]
    }

    inline fun zoneLocal(x: Int, y: Int): Int = (x and 0x7) or ((y and 0x7) shl 3)
}
