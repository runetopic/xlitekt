package xlitekt.game.actor.movement

import xlitekt.game.actor.Actor
import xlitekt.game.world.map.location.Location
import xlitekt.game.world.map.location.direction
import java.util.LinkedList
import java.util.Queue

enum class MovementSpeed(val stepCount: Int) {
    WALK(1),
    RUN(2)
}

data class WalkStep(
    val location: Location,
    val direction: Direction
)

class Movement(
    private val actor: Actor,
    private val path: Queue<Location> = LinkedList(),
    var currentWalkStep: WalkStep? = null
) : Queue<Location> by path {

    fun process() {
        val location = actor.location
        if (location.compareTo(currentWalkStep?.location ?: location) == 1) {
            currentWalkStep = null
        }
        if (path.isNotEmpty() && currentWalkStep == null) {
            path.poll()?.let {
                val direction = location.direction(it)
                currentWalkStep = WalkStep(it, direction)
                actor.movementDirection = direction
            }
        }
        if (currentWalkStep != null) {
            actor.location = location.transform(actor.movementDirection)
        }
    }

    fun reset() {
        path.clear()
    }
}
