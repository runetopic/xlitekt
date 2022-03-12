package xlitekt.game.actor.movement

import xlitekt.game.world.map.location.Location

/**
 * @author Jordan Abraham
 */
data class WaypointStep(
    val speed: MovementSpeed,
    val location: Location,
)
