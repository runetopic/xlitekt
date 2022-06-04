package xlitekt.game.content.projectile

import xlitekt.game.world.map.Location

/**
 * @author Jordan Abraham
 */
data class Projectile(
    val id: Int,
    val startLocation: Location,
    val endLocation: Location,
    val startHeight: Int,
    val endHeight: Int,
    val delay: Int,
    val angle: Int,
    val steepness: Int
) {
    fun distanceX() = endLocation.x - startLocation.x
    fun distanceZ() = endLocation.z - startLocation.z
}
