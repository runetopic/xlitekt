package xlitekt.game.actor.movement

import it.unimi.dsi.fastutil.ints.IntArrayFIFOQueue
import it.unimi.dsi.fastutil.ints.IntPriorityQueue
import xlitekt.game.actor.Actor
import xlitekt.game.actor.angleTo
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.temporaryMovementType
import xlitekt.game.world.map.Location
import xlitekt.game.world.map.directionTo
import java.util.Optional
import kotlin.math.min
import kotlin.math.sign

/**
 * @author Jordan Abraham
 */
class Movement {
    var movementSpeed = MovementSpeed.None
    var movementRequest = Optional.empty<MovementRequest>()

    private val checkpoints: IntPriorityQueue = IntArrayFIFOQueue()
    private val steps: IntPriorityQueue = IntArrayFIFOQueue()
    private var teleporting = false
    private var direction = Direction.BasicSouth

    /**
     * Process any pending movement for a actor.
     * @param actor The actor to process this movement for.
     */
    fun process(actor: Actor): MovementStep? {
        val currentLocation = actor.location
        val previousLocation = actor.previousLocation
        actor.previousLocation = currentLocation
        if (checkpoints.isEmpty && steps.isEmpty) {
            return null
        }
        if (steps.isEmpty) {
            tryEnqueueSteps(currentLocation)
        }
        // Poll the first step to move to.
        var step = if (steps.isEmpty) return null else steps.dequeueInt()
        // The current speed of the first step.
        val initialSpeed = if (teleporting) MovementSpeed.Teleporting else movementSpeed
        var modifiedSpeed = initialSpeed
        when (initialSpeed) {
            MovementSpeed.Teleporting -> {
                teleporting = false
                direction = Direction.BasicSouth
                // Only players will do this.
                if (actor is Player) {
                    val type = MovementSpeed.Teleporting.id
                    actor.temporaryMovementType { type }
                    actor.angleTo(currentLocation)
                }
            }
            MovementSpeed.Walking, MovementSpeed.Running -> if (initialSpeed == MovementSpeed.Running) {
                // If the player is running, then we poll the second step to move to.
                step = if (steps.isEmpty) {
                    // If the second step is unavailable, then we try to queue more and poll for the second step again.
                    tryEnqueueSteps(Location(step))
                    if (steps.isEmpty) {
                        // If a second step is not able to be found, then we adjust the step the player has to walking.
                        modifiedSpeed = MovementSpeed.Walking
                        // Apply this mask to the player to show them actually walking.
                        val type = MovementSpeed.Walking.id
                        actor.temporaryMovementType { type }
                        step
                    } else {
                        val it = steps.dequeueInt()
                        // If the new-found second step is within walking distance, then we have to adjust the step speed to walking instead of running.
                        // We do not use the movement type mask here because we want the player to look like they are running but using walking opcodes.
                        if (currentLocation.directionTo(Location(it)).isFourPointCardinal) {
                            modifiedSpeed = MovementSpeed.Walking
                        }
                        it
                    }
                } else steps.dequeueInt()
            }
        }
        return MovementStep(
            speed = modifiedSpeed,
            location = Location(step),
            previousLocation = previousLocation,
            direction = if (initialSpeed == MovementSpeed.Teleporting) Direction.BasicSouth else currentLocation.directionTo(Location(step))
        )
    }

    /**
     * Routes this movement to a movement request.
     * Queues checkpoints from this movement request.
     * @param request The movement request.
     */
    fun route(request: MovementRequest) {
        val waypoints = request.waypoints
        if (waypoints.isEmpty) {
            if (!request.failed && !request.alternative) {
                request.reachAction?.invoke()
            }
            return
        }
        movementRequest = Optional.of(request)
        for (i in 0..min(waypoints.lastIndex, 24)) {
            checkpoints.enqueue(waypoints.getInt(i))
        }
    }

    /**
     * Routes this movement to a single location with optional teleport speed.
     */
    fun route(location: Location, teleport: Boolean) {
        checkpoints.enqueue(location.packedLocation)
        if (teleport) teleporting = true
    }

    /**
     * Queues steps from a dequeued checkpoint.
     * @param location The starting location to base the amount of steps from to the dequeued checkpoint.
     */
    private fun tryEnqueueSteps(location: Location) {
        if (checkpoints.isEmpty) return
        steps.clear()
        val waypoint = Location(checkpoints.dequeueInt())
        if (teleporting) {
            steps.enqueue(waypoint.packedLocation)
            return
        }
        val currentX = location.x
        val currentZ = location.z
        val waypointX = waypoint.x
        val waypointZ = waypoint.z
        enqueueSteps(currentX, currentZ, waypointX, waypointZ, (waypointX - currentX).sign, (waypointZ - currentZ).sign, location.level)
    }

    private tailrec fun enqueueSteps(currentX: Int, currentZ: Int, waypointX: Int, waypointZ: Int, xSign: Int, zSign: Int, level: Int) {
        if (currentX == waypointX && currentZ == waypointZ) return
        steps.enqueue(Location(currentX + xSign, currentZ + zSign, level).packedLocation)
        return enqueueSteps(currentX + xSign, currentZ + zSign, waypointX, waypointZ, xSign, zSign, level)
    }

    fun hasSteps(): Boolean = !steps.isEmpty
    fun hasCheckpoints(): Boolean = !checkpoints.isEmpty

    fun isMoving(): Boolean = !hasSteps() || !hasCheckpoints()

    /**
     * Resets this movement.
     */
    fun reset() {
        checkpoints.clear()
        steps.clear()
        teleporting = false
        movementRequest = Optional.empty()
    }
}
