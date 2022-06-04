package xlitekt.game.actor.movement

import it.unimi.dsi.fastutil.ints.IntArrayFIFOQueue
import it.unimi.dsi.fastutil.ints.IntList
import it.unimi.dsi.fastutil.ints.IntPriorityQueue
import xlitekt.game.actor.Actor
import xlitekt.game.actor.faceAngle
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.temporaryMovementType
import xlitekt.game.world.map.Location
import xlitekt.game.world.map.directionTo
import kotlin.math.min
import kotlin.math.sign

/**
 * @author Jordan Abraham
 */
class Movement {
    var movementSpeed = MovementSpeed.None

    private val checkpoints: IntPriorityQueue = IntArrayFIFOQueue()
    private val steps: IntPriorityQueue = IntArrayFIFOQueue()
    private var teleporting = false
    private var direction: Direction = Direction.South

    fun process(actor: Actor, currentLocation: Location): MovementStep? {
        val previousLocation = actor.previousLocation
        actor.previousLocation = currentLocation
        if (checkpoints.isEmpty && steps.isEmpty) {
            return null
        }
        if (steps.isEmpty) {
            queueDestinationSteps(currentLocation)
            if (steps.isEmpty && actor is Player) {
                val direction = previousLocation.directionTo(currentLocation)
                if (this.direction != direction) {
                    this.direction = direction
                    actor.faceAngle(direction::angle)
                }
            }
        }
        // Poll the first step to move to.
        var step = if (steps.isEmpty) return null else steps.dequeueInt()
        // The current speed of the first step.
        val initialSpeed = if (teleporting) MovementSpeed.Teleporting else movementSpeed
        var modifiedSpeed = initialSpeed
        when (initialSpeed) {
            MovementSpeed.Teleporting -> {
                teleporting = false
                val direction = Direction.South
                this.direction = direction
                // Only players will do this.
                if (actor is Player) {
                    actor.temporaryMovementType(MovementSpeed.Teleporting::id)
                    actor.faceAngle(direction::angle)
                }
            }
            MovementSpeed.Walking, MovementSpeed.Running -> if (initialSpeed == MovementSpeed.Running) {
                // If the player is running, then we poll the second step to move to.
                step = if (steps.isEmpty) {
                    // If the second step is unavailable, then we try to queue more and poll for the second step again.
                    queueDestinationSteps(Location(step))
                    if (steps.isEmpty) {
                        // If a second step is not able to be found, then we adjust the step the player has to walking.
                        modifiedSpeed = MovementSpeed.Walking
                        // Apply this mask to the player to show them actually walking.
                        actor.temporaryMovementType(MovementSpeed.Walking::id)
                        step
                    } else {
                        val it = steps.dequeueInt()
                        // If the new-found second step is within walking distance, then we have to adjust the step speed to walking instead of running.
                        // We do not use the movement type mask here because we want the player to look like they are running but using walking opcodes.
                        if (currentLocation.directionTo(Location(it)).fourPointCardinalDirection()) {
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
            direction = if (initialSpeed == MovementSpeed.Teleporting) Direction.South else currentLocation.directionTo(Location(step))
        )
    }

    fun route(list: IntList) {
        // https://oldschool.runescape.wiki/w/Pathfinding#The_checkpoint_tiles_and_destination_tile
        for (i in 0..min(list.lastIndex, 24)) {
            checkpoints.enqueue(list.getInt(i))
        }
    }

    fun route(location: Location, teleport: Boolean) {
        checkpoints.enqueue(location.packedLocation)
        if (teleport) teleporting = true
    }

    private fun queueDestinationSteps(location: Location) {
        if (checkpoints.isEmpty) return
        steps.clear()
        val waypoint = Location(checkpoints.dequeueInt())
        if (teleporting) {
            steps.enqueue(waypoint.packedLocation)
            return
        }
        var currentX = location.x
        var currentZ = location.z
        val waypointX = waypoint.x
        val waypointZ = waypoint.z
        val xSign = (waypointX - currentX).sign
        val zSign = (waypointZ - currentZ).sign
        while (currentX != waypointX || currentZ != waypointZ) {
            currentX += xSign
            currentZ += zSign
            steps.enqueue(Location(currentX, currentZ, location.level).packedLocation)
        }
    }

    fun reset() {
        checkpoints.clear()
        steps.clear()
        teleporting = false
    }
}
