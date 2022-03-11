package xlitekt.game.actor.movement

import xlitekt.game.actor.Actor
import xlitekt.game.world.map.location.Location
import xlitekt.game.world.map.location.directionTo
import xlitekt.game.world.map.location.withinDistance
import java.util.Queue
import java.util.concurrent.LinkedBlockingQueue
import kotlin.math.sign

enum class MovementSpeed { WALK, RUN }

data class MovementStep(
    val speed: MovementSpeed,
    val location: Location,
    val direction: Direction
)

class Movement(
    private val actor: Actor,
    private val waypoints: Queue<Location> = LinkedBlockingQueue(),
    private val steps: Queue<Location> = LinkedBlockingQueue()
) : Queue<Location> by waypoints {
    var previousStep: MovementStep? = null

    private var movementSpeed = MovementSpeed.WALK

    fun process(): MovementStep? {
        if (isEmpty() && steps.isEmpty()) return null
        if (steps.isEmpty()) queueStepsToWaypoint()
        val previousLocation = actor.location
        actor.previousLocation = previousLocation
        val firstStep = steps.poll() ?: return null
        actor.location = firstStep
        var speed = MovementSpeed.WALK
        if (movementSpeed == MovementSpeed.RUN) {
            when (val secondStep = steps.poll()) {
                // The player will "walk" on steps that are (steps.size % 3 == 0) so this is for that edge case.
                null -> if (isEmpty() && previousStep?.location?.withinDistance(firstStep, 1) == false) actor.setTemporaryMovementType(1)
                else -> {
                    speed = MovementSpeed.RUN
                    actor.location = secondStep
                }
            }
        }
        return MovementStep(speed, previousLocation, previousLocation.directionTo(actor.location)).also { previousStep = it }
    }

    private fun queueStepsToWaypoint() {
        if (waypoints.isEmpty()) return
        steps.clear()
        val waypoint = waypoints.poll()
        val location = actor.location
        var currentX = location.x
        var currentZ = location.z
        val waypointX = waypoint.x
        val waypointZ = waypoint.z
        val xSign = (waypointX - currentX).sign
        val zSign = (waypointZ - currentZ).sign
        var count = 0
        while (currentX != waypointX || currentZ != waypointZ) {
            currentX += xSign
            currentZ += zSign
            steps.add(Location(currentX, currentZ))
            if (++count > 25) break
        }
    }

    fun reset() {
        waypoints.clear()
        steps.clear()
    }

    fun toggleRun() {
        movementSpeed = if (movementSpeed == MovementSpeed.RUN) MovementSpeed.WALK else MovementSpeed.RUN
        actor.movementType(movementSpeed == MovementSpeed.RUN)
    }
}
