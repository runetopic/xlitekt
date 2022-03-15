package xlitekt.game.actor.movement

import xlitekt.game.actor.Actor
import xlitekt.game.world.map.location.Location
import xlitekt.game.world.map.location.directionTo
import xlitekt.game.world.map.location.withinDistance
import java.util.Deque
import java.util.LinkedList
import kotlin.math.sign

class Movement(
    private val actor: Actor,
    private val waypoints: LinkedList<Location> = LinkedList(),
) : Deque<Location> by waypoints {
    var currentWaypoint: Location? = null
    private var movementSpeed = MovementSpeed.WALKING
    private var teleporting = false

    fun process(currentLocation: Location): MovementStep? {
        val previousLocation = actor.previousLocation
        actor.previousLocation = currentLocation
        if (isEmpty() && currentWaypoint == null) {
            println("RETURN NULL 1")
            return null
        }
        if (atWaypoint()) {
            currentWaypoint = waypoints.poll() ?: run {
                println("Fuck my dick return null 2")
                return null
            }
        }
        return nextWaypointStep(currentLocation).run {
            MovementStep(
                speed = speed,
                location = location,
                direction = if (speed == MovementSpeed.TELEPORTING) Direction.South else currentLocation.directionTo(location)
            ).also {
                when {
                    it.speed.isTeleporting() -> {
                        actor.temporaryMovementType(MovementSpeed.TELEPORTING.id)
                        teleporting = false
                    }
                    it.speed.isWalking() && isEmpty() && previousLocation?.withinDistance(location, 1) == false -> {
                        actor.temporaryMovementType(MovementSpeed.WALKING.id)
                    }
                }
                actor.location = location
            }
        }
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

    private fun nextWaypointStep(location: Location): WaypointStep {
        val waypoint = currentWaypoint ?: throw IllegalStateException("Current waypoint is null.")
        if (teleporting) return WaypointStep(MovementSpeed.TELEPORTING, waypoint)
        val waypointX = waypoint.x
        val waypointZ = waypoint.z
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
            } else {
                currentWaypoint = poll()
                if (currentWaypoint != null) {
                    val waypoint = currentWaypoint ?: throw IllegalStateException("Current waypoint is null.")
                    if (teleporting) return WaypointStep(MovementSpeed.TELEPORTING, waypoint)
                    val waypointX = waypoint.x
                    val waypointZ = waypoint.z
                    val xSign = (waypointX - currentX).sign
                    val zSign = (waypointZ - currentZ).sign
                    if (currentX != waypointX || currentZ != waypointZ) {
                        currentX += xSign
                        currentZ += zSign
                    }
                }
            }
        }
        return WaypointStep(speed, Location(currentX, currentZ))
    }

    private fun atWaypoint(): Boolean {
        if (teleporting) return true
        if (currentWaypoint == null) return true
        return currentWaypoint!!.x - actor.location.x == 0 && currentWaypoint!!.z - actor.location.z == 0
    }

    fun reset() {
        waypoints.clear()
        currentWaypoint = null
        teleporting = false
    }

    fun toggleRun() {
        movementSpeed = if (movementSpeed.isRunning()) MovementSpeed.WALKING else MovementSpeed.RUNNING
        actor.movementType(movementSpeed.isRunning())
    }
}
