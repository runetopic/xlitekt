package com.runetopic.xlitekt.game.actor.movement

import com.runetopic.xlitekt.game.world.map.location.Location

private const val NEUTRAL_UNIT = 0
private const val POSITIVE_UNIT = 1
private const val NEGATIVE_UNIT = -1

sealed class Direction(val x: Int = NEUTRAL_UNIT, val y: Int = NEUTRAL_UNIT) {
    object North : Direction(y = POSITIVE_UNIT)
    object East : Direction(x = POSITIVE_UNIT)
    object South : Direction(y = NEGATIVE_UNIT)
    object West : Direction(x = NEGATIVE_UNIT)
    object NorthEast : Direction(x = POSITIVE_UNIT, y = POSITIVE_UNIT)
    object SouthEast : Direction(x = POSITIVE_UNIT, y = NEGATIVE_UNIT)
    object SouthWest : Direction(x = NEGATIVE_UNIT, y = NEGATIVE_UNIT)
    object NorthWest : Direction(x = NEGATIVE_UNIT, y = POSITIVE_UNIT)

    override fun toString(): String = javaClass.simpleName
}

fun Location.translate(direction: Direction) = transform(direction.x, direction.y)
