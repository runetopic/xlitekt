package com.runetopic.xlitekt.game.actor.movement

import com.runetopic.xlitekt.game.actor.Actor
import com.runetopic.xlitekt.game.world.map.location.Location
import com.runetopic.xlitekt.game.world.map.location.direction
import java.util.LinkedList
import java.util.Queue

enum class MovementSpeed(val stepCount: Int) {
    WALK(1),
    RUN(2)
}

data class WalkStep(
    val location: Location,
    val direction: Direction
)

class Movement(
    private val actor: Actor,
    private val path: Queue<Location> = LinkedList(),
    private val steps: MutableList<WalkStep> = mutableListOf()
) : Queue<Location> by path {

    fun process() {
        var lastLocation = actor.location
        repeat(actor.movementSpeed.stepCount) {
            val destination = poll() ?: return@repeat
            val direction = lastLocation.direction(destination)
            steps.add(WalkStep(destination, direction))
            lastLocation = destination
        }
    }
}
