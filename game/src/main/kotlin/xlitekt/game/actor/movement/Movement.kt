package xlitekt.game.actor.movement

import xlitekt.game.actor.Actor
import xlitekt.game.actor.faceAngle
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.temporaryMovementType
import xlitekt.game.world.map.location.Location
import xlitekt.game.world.map.location.directionTo
import java.util.Deque
import java.util.LinkedList
import kotlin.math.sign

/**
 * @author Jordan Abraham
 */
class Movement(
    private val checkpoints: LinkedList<Location> = LinkedList(),
) : Deque<Location> by checkpoints {
    var movementSpeed = MovementSpeed.WALKING

    private val steps = LinkedList<Location>()
    private var teleporting = false
    private var direction: Direction = Direction.South

    fun process(actor: Actor, currentLocation: Location): MovementStep? {
        val previousLocation = actor.previousLocation
        actor.previousLocation = currentLocation
        if (isEmpty() && steps.isEmpty()) {
            return null
        }
        if (steps.isEmpty()) {
            queueDestinationSteps(currentLocation)
            if (steps.isEmpty() && actor is Player) {
                val direction = previousLocation?.directionTo(currentLocation)
                if (direction != null && this.direction != direction) {
                    this.direction = direction
                    actor.faceAngle(direction::angle)
                }
            }
        }
        // Poll the first step to move to.
        var step = steps.poll() ?: return null
        // The current speed of the first step.
        val initialSpeed = if (teleporting) MovementSpeed.TELEPORTING else movementSpeed
        var modifiedSpeed = initialSpeed
        when (initialSpeed) {
            MovementSpeed.TELEPORTING -> {
                teleporting = false
                val direction = Direction.South
                this.direction = direction
                // Only players will do this.
                if (actor is Player) {
                    actor.temporaryMovementType(MovementSpeed.TELEPORTING::id)
                    actor.faceAngle(direction::angle)
                }
            }
            MovementSpeed.WALKING, MovementSpeed.RUNNING -> if (initialSpeed == MovementSpeed.RUNNING) {
                // If the player is running, then we poll the second step to move to.
                step = steps.poll() ?: run {
                    // If the second step is unavailable, then we try to queue more and poll for the second step again.
                    queueDestinationSteps(step)
                    steps.poll()?.let {
                        // If the new-found second step is within walking distance, then we have to adjust the step speed to walking instead of running.
                        // We do not use the movement type mask here because we want the player to look like they are running but using walking opcodes.
                        if (currentLocation.directionTo(it).fourPointCardinalDirection()) {
                            modifiedSpeed = MovementSpeed.WALKING
                        }
                        it
                    } ?: run {
                        // If a second step is not able to be found, then we adjust the step the player has to walking.
                        modifiedSpeed = MovementSpeed.WALKING
                        // Apply this mask to the player to show them actually walking.
                        actor.temporaryMovementType(MovementSpeed.WALKING::id)
                        step
                    }
                }
            }
        }
        return MovementStep(
            speed = modifiedSpeed,
            location = step,
            previousLocation = previousLocation ?: step,
            direction = if (initialSpeed == MovementSpeed.TELEPORTING) Direction.South else currentLocation.directionTo(step)
        )
    }

    fun route(list: List<Location>) {
        // https://oldschool.runescape.wiki/w/Pathfinding#The_checkpoint_tiles_and_destination_tile
        addAll(list.take(25))
    }

    fun route(location: Location, teleport: Boolean) {
        add(location)
        if (teleport) teleporting = true
    }

    private fun queueDestinationSteps(location: Location) {
        if (checkpoints.isEmpty()) return
        steps.clear()
        val waypoint = checkpoints.poll()
        if (teleporting) {
            steps += waypoint
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
            steps += Location(currentX, currentZ, location.level)
            // https://oldschool.runescape.wiki/w/Pathfinding#The_checkpoint_tiles_and_destination_tile
            if (steps.size >= 25) break
        }
    }

    fun reset() {
        checkpoints.clear()
        steps.clear()
        teleporting = false
    }
}
