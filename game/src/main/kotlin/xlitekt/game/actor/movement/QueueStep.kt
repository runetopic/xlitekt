package xlitekt.game.actor.movement

import xlitekt.game.world.map.location.Location

/**
 * @author Jordan Abraham
 */
data class QueueStep(
    val speed: MovementSpeed,
    val location: Location,
)
