package xlitekt.game.actor.movement

import xlitekt.game.actor.Actor
import xlitekt.game.world.map.location.Location
import xlitekt.game.world.map.location.directionTo
import xlitekt.game.world.map.location.withinDistance
import java.util.Queue
import java.util.concurrent.LinkedBlockingQueue
import kotlin.math.sign

class Movement(
    private val actor: Actor,
    private val waypoints: Queue<Location> = LinkedBlockingQueue(),
) : Queue<Location> by waypoints {
    var currentWaypoint: Location? = null
    private var previousStep: MovementStep? = null

    private var movementSpeed = MovementSpeed.WALKING

    fun process(): MovementStep? {
        if (isEmpty() && currentWaypoint == null) return null
        if (landed()) currentWaypoint = waypoints.poll() ?: return null
        val location = actor.location
        actor.previousLocation = location
        val queueStep = nextStep()
        actor.location = queueStep.location
        return when (queueStep.speed) {
            MovementSpeed.WALKING -> {
                if (isEmpty() && previousStep?.location?.withinDistance(queueStep.location, 1) == false) actor.setTemporaryMovementType(1)
                MovementStep(MovementSpeed.WALKING, location, location.directionTo(actor.location)).also { previousStep = it }
            }
            MovementSpeed.RUNNING -> MovementStep(MovementSpeed.RUNNING, location, location.directionTo(actor.location)).also { previousStep = it }
        }
    }

    private fun nextStep(): QueueStep {
        val waypoint = currentWaypoint!!
        val waypointX = waypoint.x
        val waypointZ = waypoint.z
        val location = actor.location
        var currentX = location.x
        var currentZ = location.z
        val xSign = (waypointX - currentX).sign
        val zSign = (waypointZ - currentZ).sign
        var speed = MovementSpeed.WALKING
        repeat(movementSpeed.steps) {
            if (currentX != waypointX || currentZ != waypointZ) {
                if (it == 1) speed = MovementSpeed.RUNNING
                currentX += xSign
                currentZ += zSign
            }
        }
        return QueueStep(speed, Location(currentX, currentZ))
    }

    private fun landed(): Boolean {
        if (currentWaypoint == null) return true
        return currentWaypoint!!.x - actor.location.x == 0 && currentWaypoint!!.z - actor.location.z == 0
    }

    fun reset() {
        waypoints.clear()
        currentWaypoint = null
    }

    fun toggleRun() {
        movementSpeed = if (movementSpeed == MovementSpeed.RUNNING) MovementSpeed.WALKING else MovementSpeed.RUNNING
        actor.movementType(movementSpeed == MovementSpeed.RUNNING)
    }
}
