package xlitekt.game.actor.movement

private const val NEUTRAL_UNIT = 0
private const val POSITIVE_UNIT = 1
private const val NEGATIVE_UNIT = -1

sealed class Direction(val x: Int = NEUTRAL_UNIT, val y: Int = NEUTRAL_UNIT) {
    object North : Direction(x = POSITIVE_UNIT)
    object East : Direction(y = POSITIVE_UNIT)
    object South : Direction(x = NEGATIVE_UNIT)
    object West : Direction(y = NEGATIVE_UNIT)
    object NorthEast : Direction(x = POSITIVE_UNIT, y = POSITIVE_UNIT)
    object SouthEast : Direction(x = NEGATIVE_UNIT, y = POSITIVE_UNIT)
    object SouthWest : Direction(x = NEGATIVE_UNIT, y = NEGATIVE_UNIT)
    object NorthWest : Direction(x = POSITIVE_UNIT, y = NEGATIVE_UNIT)

    override fun toString(): String = javaClass.simpleName

    fun opcode(): Int = when (this) {
        is SouthWest -> 0
        is SouthEast -> 2
        is NorthWest -> 5
        is NorthEast -> 7
        is South -> 1
        is West -> 3
        is East -> 4
        is North -> 6
    }
}
