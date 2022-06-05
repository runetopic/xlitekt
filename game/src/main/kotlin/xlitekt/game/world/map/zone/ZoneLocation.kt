package xlitekt.game.world.map.zone

import xlitekt.game.world.map.Location

/**
 * @author Kris
 * @author Jordan Abraham
 */
@JvmInline
value class ZoneLocation(
    val packedCoordinates: Int
) {
    constructor(x: Int, y: Int, z: Int = 0) : this((x and 0x7FF) or ((y and 0x7FF) shl 11) or ((z and 0x3) shl 22))

    inline val x get() = packedCoordinates and 0x7FF
    inline val z get() = (packedCoordinates shr 11) and 0x7FF
    inline val level get() = (packedCoordinates shr 22) and 0x3

    fun clone(): ZoneLocation = ZoneLocation(packedCoordinates)
    fun transform(deltaX: Int, deltaZ: Int, deltaLevel: Int = 0): ZoneLocation = ZoneLocation(x + deltaX, z + deltaZ, level + deltaLevel)
    fun toFullLocation(): Location = Location(x shl 3, z shl 3, level)

    override fun toString(): String = "ZoneLocation($x, $z, $level)"
}
