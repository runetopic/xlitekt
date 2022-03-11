package xlitekt.game.actor.movement

import java.lang.IllegalArgumentException

private const val NEUTRAL_UNIT = 0
private const val POSITIVE_UNIT = 1
private const val NEGATIVE_UNIT = -1

sealed class Direction(val x: Int = NEUTRAL_UNIT, val z: Int = NEUTRAL_UNIT) {
    object North : Direction(z = POSITIVE_UNIT)
    object East : Direction(x = POSITIVE_UNIT)
    object South : Direction(z = NEGATIVE_UNIT)
    object West : Direction(x = NEGATIVE_UNIT)
    object NorthEast : Direction(x = POSITIVE_UNIT, z = POSITIVE_UNIT)
    object SouthEast : Direction(x = POSITIVE_UNIT, z = NEGATIVE_UNIT)
    object SouthWest : Direction(x = NEGATIVE_UNIT, z = NEGATIVE_UNIT)
    object NorthWest : Direction(x = NEGATIVE_UNIT, z = POSITIVE_UNIT)
    object NorthNorthEast : Direction(x = POSITIVE_UNIT, z = POSITIVE_UNIT + 1)
    object NorthNorthWest : Direction(x = NEGATIVE_UNIT, z = POSITIVE_UNIT + 1)
    object WestNorthWest : Direction(x = NEGATIVE_UNIT - 1, z = POSITIVE_UNIT)
    object WestSouthWest : Direction(x = NEGATIVE_UNIT - 1, z = NEGATIVE_UNIT)
    object EastNorthEast : Direction(x = POSITIVE_UNIT + 1, z = POSITIVE_UNIT)
    object EastSouthEast : Direction(x = POSITIVE_UNIT + 1, z = NEGATIVE_UNIT)
    object SouthSouthEast : Direction(x = POSITIVE_UNIT, z = NEGATIVE_UNIT - 1)
    object SouthSouthWest : Direction(x = NEGATIVE_UNIT, z = NEGATIVE_UNIT - 1)

    override fun toString(): String = javaClass.simpleName

    fun opcode(useSixteenPoints: Boolean = false): Int = when (this) {
        is NorthEast -> if (useSixteenPoints) 15 else 7
        is NorthNorthEast -> 14
        is North -> if (useSixteenPoints) 13 else 6
        is NorthNorthWest -> 12
        is NorthWest -> if (useSixteenPoints) 11 else 5
        is EastNorthEast -> 10
        is WestNorthWest -> 9
        is East -> if (useSixteenPoints) 8 else 4
        is West -> if (useSixteenPoints) 7 else 3
        is EastSouthEast -> 6
        is WestSouthWest -> 5
        is SouthEast -> if (useSixteenPoints) 4 else 2
        is SouthSouthEast -> 3
        is South -> if (useSixteenPoints) 2 else 1
        is SouthSouthWest -> 1
        is SouthWest -> 0
    }

    companion object {
        fun directionFromDelta(deltaX: Int, deltaZ: Int): Direction = when {
            // 16 point direction.
            deltaX == 2 && deltaZ == 2 -> NorthEast // 15
            deltaX == 1 && deltaZ == 2 -> NorthNorthEast // 14
            deltaX == 0 && deltaZ == 2 -> North // 13
            deltaX == -1 && deltaZ == 2 -> NorthNorthWest // 12
            deltaX == -2 && deltaZ == 2 -> NorthWest // 11
            deltaX == 2 && deltaZ == 1 -> EastNorthEast // 10
            deltaX == -2 && deltaZ == 1 -> WestNorthWest // 9
            deltaX == 2 && deltaZ == 0 -> East // 8
            deltaX == -2 && deltaZ == 0 -> West // 7
            deltaX == 2 && deltaZ == -1 -> EastSouthEast // 6
            deltaX == -2 && deltaZ == -1 -> WestSouthWest // 5
            deltaX == 2 && deltaZ == -2 -> SouthEast // 4
            deltaX == 1 && deltaZ == -2 -> SouthSouthEast // 3
            deltaX == 0 && deltaZ == -2 -> South // 2
            deltaX == -1 && deltaZ == -2 -> SouthSouthWest // 1
            deltaX == -2 && deltaZ == -2 -> SouthWest // 0
            // 8 point direction.
            deltaX == 1 && deltaZ == 1 -> NorthEast // 7
            deltaX == 0 && deltaZ == 1 -> North // 6
            deltaX == -1 && deltaZ == 1 -> NorthWest // 5
            deltaX == 1 && deltaZ == 0 -> East // 4
            deltaX == -1 && deltaZ == 0 -> West // 3
            deltaX == 1 && deltaZ == -1 -> SouthEast // 2
            deltaX == 0 && deltaZ == -1 -> South // 1
            deltaX == -1 && deltaZ == -1 -> SouthWest // 0
            else -> throw IllegalArgumentException("Could not find direction for deltaX $deltaX and deltaZ $deltaZ.")
        }
    }
}
