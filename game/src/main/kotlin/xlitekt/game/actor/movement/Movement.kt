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

data class Waypoint(
    val location: Location,
    val direction: Direction
)

class Movement(
    private val actor: Actor,
    private val path: Queue<Location> = LinkedBlockingQueue(),
    private val steps: Queue<Location> = LinkedBlockingQueue()
) : Queue<Location> by path {
    var movementDirection: Direction = Direction.South
    var hasStep = false

    fun process() {
        val location = actor.location
        actor.previousLocation = location
        if (steps.isEmpty()) append()
        val nextStep = steps.poll()
        if (nextStep != null) {
            hasStep = true
            movementDirection = actor.location.directionTo(nextStep)
            actor.location = nextStep
        }
    }

    private fun append() {
        if (path.isEmpty()) return
        steps.clear()
        val step = path.poll()
        val last = actor.location
        var curX = last.x
        var curY = last.z
        val destX = step.x
        val destY = step.z
        val xSign = (destX - curX).sign
        val ySign = (destY - curY).sign
        var count = 0
        while (curX != destX || curY != destY) {
            curX += xSign
            curY += ySign
            steps.add(Location(curX, curY))
            if (++count > 25) break
        }
    }

    fun reset() {
        path.clear()
        steps.clear()
    }
}
