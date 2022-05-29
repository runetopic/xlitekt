package xlitekt.game.actor.movement

import xlitekt.game.world.map.location.Location

/**
 * @author Jordan Abraham
 */
data class MovementStep(
    val speed: MovementSpeed,
    val location: Location,
    val previousLocation: Location,
    val direction: Direction
)
