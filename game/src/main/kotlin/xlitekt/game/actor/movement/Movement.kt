package xlitekt.game.actor.movement

import xlitekt.game.actor.Actor
import xlitekt.game.world.map.location.Location
import xlitekt.game.world.map.location.directionTo
import xlitekt.game.world.map.location.withinDistance
import java.util.Deque
import java.util.LinkedList
import kotlin.math.sign

/**
 * @author Jordan Abraham
 */
class Movement(
    private val actor: Actor,
    private val waypoints: LinkedList<Location> = LinkedList(),
) : Deque<Location> by waypoints {
    private val steps = LinkedList<WaypointStep>()
    private var movementSpeed = MovementSpeed.WALKING
    private var teleporting = false
    private var direction: Direction = Direction.South

    fun process(currentLocation: Location): MovementStep? {
        actor.previousLocation = currentLocation
        if (isEmpty() && steps.isEmpty()) {
            return null
        }
        if (steps.isEmpty()) {
            queueWaypointSteps(currentLocation)
            if (steps.isEmpty()) return null
        }
        // Poll the first step to move to.
        var step = steps.poll() ?: return null
        // The current speed of the first step.
        var stepSpeed = step.speed
        when (step.speed) {
            MovementSpeed.TELEPORTING -> {
                actor.temporaryMovementType(MovementSpeed.TELEPORTING.id)
                teleporting = false
            }
            MovementSpeed.WALKING, MovementSpeed.RUNNING -> if (step.speed == MovementSpeed.RUNNING) {
                // If the player is running, then we poll the second step to move to.
                step = steps.poll() ?: run {
                    // If the second step is unavailable, then we try to queue more and poll for the second step again.
                    queueWaypointSteps(step.location)
                    steps.poll()?.let {
                        // If the new-found second step is within walking distance, then we have to adjust the step speed to walking instead of running.
                        // We do not use the movement type mask here because we want the player to look like they are running but using walking opcodes.
                        if (it.location.withinDistance(currentLocation, 1)) {
                            stepSpeed = MovementSpeed.WALKING
                        }
                        it
                    } ?: run {
                        // If a second step is not able to be found, then we adjust the step the player has to walking.
                        stepSpeed = MovementSpeed.WALKING
                        // Apply this mask to the player to show them actually walking.
                        actor.temporaryMovementType(MovementSpeed.WALKING.id)
                        step
                    }
                }
            }
        }
        actor.location = step.location
        val direction = if (step.speed == MovementSpeed.TELEPORTING) Direction.South else currentLocation.directionTo(step.location)
        if (this.direction != direction) {
            this.direction = direction
            actor.faceDirection(direction.angle())
        }
        return MovementStep(stepSpeed, step.location, direction)
    }

    fun route(list: List<Location>) {
        reset()
        addAll(list)
    }

    fun route(location: Location, teleport: Boolean = false) {
        reset()
        add(location)
        if (teleport) teleporting = true
    }

    private fun queueWaypointSteps(location: Location) {
        if (waypoints.isEmpty()) return
        steps.clear()
        val waypoint = waypoints.poll()
        if (teleporting) {
            steps.add(WaypointStep(MovementSpeed.TELEPORTING, waypoint))
            return
        }
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
            steps.add(WaypointStep(movementSpeed, Location(currentX, currentZ)))
            if (++count > 25) break
        }
    }

    private fun reset() {
        waypoints.clear()
        steps.clear()
        teleporting = false
    }

    fun toggleRun() {
        movementSpeed = if (movementSpeed.isRunning()) MovementSpeed.WALKING else MovementSpeed.RUNNING
        actor.movementType(movementSpeed.isRunning())
    }
}
