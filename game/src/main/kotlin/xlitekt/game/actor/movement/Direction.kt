package xlitekt.game.actor.movement

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

    fun opcode(): Int = when (this) {
        is SouthWest -> 0
        is South -> 1
        is SouthEast -> 2
        is West -> 3
        is East -> 4
        is NorthWest -> 5
        is North -> 6
        is NorthEast -> 7
    }

    companion object {
        fun fromDeltaXZ(deltaX: Int, deltaZ: Int): Direction = when {
            deltaX > 0 && deltaZ > 0 -> NorthEast
            deltaX > 0 && deltaZ < 0 -> SouthEast
            deltaX < 0 && deltaZ > 0 -> NorthWest
            deltaX < 0 && deltaZ < 0 -> SouthWest
            deltaX < 0 -> West
            deltaX > 0 -> East
            deltaZ > 0 -> North
            else -> South
        }
    }
}
