package xlitekt.game.actor.movement

import xlitekt.game.world.map.location.Location

/**
 * @author Jordan Abraham
 */
data class MovementStep(
    val speed: MovementSpeed? = null,
    val location: Location? = null,
    val direction: Direction? = null
)

fun emptyStep() = MovementStep()
fun MovementStep.isValid() = speed != null && location != null && direction != null
