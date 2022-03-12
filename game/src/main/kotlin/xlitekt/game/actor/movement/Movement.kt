package xlitekt.game.actor.movement

import java.util.Deque
import java.util.LinkedList
import xlitekt.game.actor.Actor
import xlitekt.game.world.map.location.Location
import xlitekt.game.world.map.location.directionTo
import xlitekt.game.world.map.location.withinDistance
import kotlin.math.sign

class Movement(
    private val actor: Actor,
    private val waypoints: LinkedList<Location> = LinkedList(),
) : Deque<Location> by waypoints {
    var currentWaypoint: Location? = null
    private var movementSpeed = MovementSpeed.WALKING

    fun process(currentLocation: Location): MovementStep? {
        if (isEmpty() && currentWaypoint == null) return null
        if (atWaypoint()) currentWaypoint = waypoints.poll() ?: return null
        val previousLocation = actor.previousLocation
        actor.previousLocation = currentLocation
        return nextWaypointStep(currentLocation).run {
            MovementStep(speed, location, currentLocation.directionTo(location)).also {
                // This check is for (steps.size % 3 == 0) the player will "walk" on the last step.
                if (it.speed.isWalking() && isEmpty() && previousLocation?.withinDistance(location, 1) == false) {
                    actor.temporaryMovementType(MovementSpeed.WALKING.id)
                }
                actor.location = location
            }
        }
    }

    private fun nextWaypointStep(location: Location): WaypointStep {
        val waypoint = currentWaypoint!!
        val waypointX = waypoint.x
        val waypointZ = waypoint.z
        // val location = actor.location
        var currentX = location.x
        var currentZ = location.z
        val xSign = (waypointX - currentX).sign
        val zSign = (waypointZ - currentZ).sign
        var speed = MovementSpeed.WALKING
        repeat(movementSpeed.id) {
            if (currentX != waypointX || currentZ != waypointZ) {
                if (it == 1) speed = MovementSpeed.RUNNING
                currentX += xSign
                currentZ += zSign
            }
        }
        return WaypointStep(speed, Location(currentX, currentZ))
    }

    private fun atWaypoint(): Boolean {
        if (currentWaypoint == null) return true
        return currentWaypoint!!.x - actor.location.x == 0 && currentWaypoint!!.z - actor.location.z == 0
    }

    fun reset() {
        waypoints.clear()
        currentWaypoint = null
    }

    fun toggleRun() {
        movementSpeed = if (movementSpeed.isRunning()) MovementSpeed.WALKING else MovementSpeed.RUNNING
        actor.movementType(movementSpeed.isRunning())
    }
}
