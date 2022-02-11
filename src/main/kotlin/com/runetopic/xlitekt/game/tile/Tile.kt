package com.runetopic.xlitekt.game.tile

import com.runetopic.xlitekt.game.actor.player.Player

@JvmInline
value class Tile(val coordinates: Int) {
    constructor(
        x: Int = 0,
        z: Int = 0,
        plane: Int = 0
    ) : this(z + (x shl 14) + (plane shl 28))

    val x: Int get() = (coordinates shr 14) and 0x3FFF
    val z: Int get() = coordinates and 0x3FFF
    val plane: Int get() = (coordinates shr 28) and 0x3
    val regionX: Int get() = (x shr 6)
    val regionZ: Int get() = (z shr 6)
    val chunkX: Int get() = (x shr 3)
    val chunkZ: Int get() = (z shr 3)
    val regionCoordinates: Int get() = z shr 13 or (x shr 13 shl 8) or (plane shl 16)
    val xInRegion: Int get() = (x and 0x3F)
    val zInRegion: Int get() = (z and 0x3F)
}

fun Tile.withinDistance(other: Player, distance: Int = 14): Boolean {
    if (other.tile.plane != plane) return false
    val deltaX = other.tile.x - x
    val deltaY = other.tile.z - z
    return deltaX <= distance && deltaX >= -distance && deltaY <= distance && deltaY >= -distance
}
