package xlitekt.game.actor.movement

import xlitekt.game.actor.Actor
import xlitekt.game.world.map.location.Location
import xlitekt.game.world.map.location.directionTo
import java.util.Queue
import java.util.concurrent.LinkedBlockingQueue
import kotlin.math.sign

enum class MovementSpeed(val stepCount: Int) {
    WALK(1),
    RUN(2)
}

data class MovementStep(
    val location: Location,
    val direction: Direction
)

class Movement(
    private val actor: Actor,
    private val waypoints: Queue<Location> = LinkedBlockingQueue(),
    private val steps: Queue<Location> = LinkedBlockingQueue()
) : Queue<Location> by waypoints {
    private var currentMovementStep: MovementStep? = null

    fun process() {
        currentMovementStep = null
        val location = actor.location
        actor.previousLocation = location
        if (waypoints.isNotEmpty() && steps.isEmpty()) queueStepsToWaypoint()
        if (steps.isEmpty()) return
        currentMovementStep = steps.poll().let {
            MovementStep(it, location.directionTo(it))
        }.also {
            actor.location = it.location
        }
    }

    fun hasMovementStep() = currentMovementStep != null
    fun movementStepDirection() = currentMovementStep?.direction

    private fun queueStepsToWaypoint() {
        steps.clear()
        val waypoint = waypoints.poll()
        val location = actor.location
        var currentX = location.x
        var currentZ = location.z
        val waypointX = waypoint.x
        val waypointZ = waypoint.z
        val xSign = (waypointX - currentX).sign
        val ySign = (waypointZ - currentZ).sign
        var count = 0
        while (currentX != waypointX || currentZ != waypointZ) {
            currentX += xSign
            currentZ += ySign
            steps.add(Location(currentX, currentZ))
            if (++count > 25) break
        }
    }

    fun reset() {
        waypoints.clear()
        steps.clear()
        currentMovementStep = null
    }
}
