package com.runetopic.xlitekt.game.world.map.location

/**
 * @author Kris
 */
@JvmInline
value class ZoneLocation(val packedCoordinates: Int) {
    constructor(x: Int, y: Int, z: Int = 0) : this((x and 0x7FF) or ((y and 0x7FF) shl 11) or ((z and 0x3) shl 22))

    val x: Int
        get() = packedCoordinates and 0x7FF
    val z: Int
        get() = (packedCoordinates shr 11) and 0x7FF
    val level: Int
        get() = (packedCoordinates shr 22) and 0x3

    fun clone(): ZoneLocation = ZoneLocation(packedCoordinates)
    fun transform(deltaX: Int, deltaY: Int, deltaZ: Int = 0): ZoneLocation = ZoneLocation(x + deltaX, z + deltaY, level + deltaZ)
    fun toFullLocation(): Location = Location(x shl 3, z shl 3, level)
    override fun toString(): String = "ZoneLocation($x, $z, $level)"
}
