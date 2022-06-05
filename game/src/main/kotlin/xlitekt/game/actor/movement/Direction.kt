package xlitekt.game.actor.movement

/**
 * @author Jordan Abraham
 */
@JvmInline
value class Direction(
    private val packedDirection: Int
) {
    constructor(dx: Int, dz: Int) : this((dx and 0xff shl 8) or (dz and 0xff))

    val deltaX get() = (packedDirection shr 8 and 0xff).let { if (it > Byte.MAX_VALUE) it - 0x100 else it }
    val deltaZ get() = (packedDirection and 0xff).let { if (it > Byte.MAX_VALUE) it - 0x100 else it }

    /**
     * Returns the corresponding opcode used for this direction for player movement.
     * @param useSixteenPoints Walking uses eight point cardinal direction and running uses sixteen point cardinal direction.
     *
     * Running direction opcodes.
     *  ______________________
     * | [11][12][13][14][15] |
     * | [09][##][##][##][10] |
     * | [07][##][XX][##][08] |
     * | [05][##][##][##][06] |
     * | [00][01][02][03][04] |
     *  ----------------------
     *
     * Walking direction opcodes.
     *  ______________
     * | [05][06][07] |
     * | [03][XX][04] |
     * | [00][01][02] |
     *  --------------
     */
    val opcodeForPlayerDirection get() = when (this) {
        NorthEast16 -> 15
        NorthNorthEast16 -> 14
        North16 -> 13
        NorthNorthWest16 -> 12
        NorthWest16 -> 11
        EastNorthEast16 -> 10
        WestNorthWest16 -> 9
        East16 -> 8
        West16, NorthEast8 -> 7
        EastSouthEast16, North8 -> 6
        WestSouthWest16, NorthWest8 -> 5
        SouthEast16, East8 -> 4
        SouthSouthEast16, West8 -> 3
        South16, SouthEast8 -> 2
        SouthSouthWest16, South8 -> 1
        SouthWest16, SouthWest8 -> 0
        else -> throw IllegalStateException("Direction opcode not found for player. Direction was $this")
    }

    /**
     * Returns the corresponding opcode used for this direction for npc movement.
     *  ______________
     * | [00][01][02] |
     * | [03][XX][04] |
     * | [05][06][07] |
     *  --------------
     */
    val opcodeForNPCDirection get() = when (this) {
        // TODO Running support.
        SouthEast8 -> 7
        South8 -> 6
        SouthWest8 -> 5
        East8 -> 4
        West8 -> 3
        NorthEast8 -> 2
        North8 -> 1
        NorthWest8 -> 0
        else -> throw IllegalStateException("Direction opcode not found for npc. Direction was $this")
    }

    /**
     * Returns the angle associated with this direction.
     */
    fun angle() = when (this) {
        NorthWest16, NorthWest8 -> 768
        NorthNorthWest16 -> 896
        North16, North8 -> 1024
        NorthNorthEast16 -> 1152
        NorthEast16, NorthEast8 -> 1280
        WestNorthWest16 -> 640
        EastNorthEast16 -> 1408
        West16, West8 -> 512
        East16, East8 -> 1536
        WestSouthWest16 -> 384
        EastSouthEast16 -> 1664
        SouthWest16, SouthWest8 -> 256
        SouthSouthWest16 -> 128
        South16, South8 -> 2048
        SouthSouthEast16 -> 1920
        SouthEast16, SouthEast8 -> 1792
        else -> 2048 // Default to south.
    }

    fun fourPointCardinalDirection() = angle() % 256 == 0

    companion object {
        private val NorthEast16 = Direction(2, 2)
        private val NorthEast8 = Direction(1, 1)
        private val NorthNorthEast16 = Direction(1, 2)
        private val North16 = Direction(0, 2)
        private val North8 = Direction(0, 1)
        private val NorthNorthWest16 = Direction(-1, 2)
        private val NorthWest16 = Direction(-2, 2)
        private val NorthWest8 = Direction(-1, 1)
        private val EastNorthEast16 = Direction(2, 1)
        private val WestNorthWest16 = Direction(-2, 1)
        private val East16 = Direction(2, 0)
        private val East8 = Direction(1, 0)
        private val West16 = Direction(-2, 0)
        private val West8 = Direction(-1, 0)
        private val EastSouthEast16 = Direction(2, -1)
        private val WestSouthWest16 = Direction(-2, -1)
        private val SouthEast16 = Direction(2, -2)
        private val SouthEast8 = Direction(1, -1)
        private val SouthSouthEast16 = Direction(1, -2)
        private val South16 = Direction(0, -2)
        private val South8 = Direction(0, -1)
        private val SouthSouthWest16 = Direction(-1, -2)
        private val SouthWest16 = Direction(-2, -2)
        private val SouthWest8 = Direction(-1, -1)

        val BasicNorth = North8
        val BasicEast = East8
        val BasicWest = West8
        val BasicSouth = South8

        val northEast = setOf(NorthEast16, NorthEast8)
        val northNorthEast = setOf(NorthNorthEast16)
        val north = listOf(North16, North8)
        val northNorthWest = setOf(NorthNorthWest16)
        val northWest = setOf(NorthWest16, NorthWest8)
        val eastNorthEast = setOf(EastNorthEast16)
        val westNorthWest = setOf(WestNorthWest16)
        val east = setOf(East16, East8)
        val west = setOf(West16, West8)
        val eastSouthEast = setOf(EastSouthEast16)
        val westSouthWest = setOf(WestSouthWest16)
        val southEast = setOf(SouthEast16, SouthEast8)
        val southSouthEast = setOf(SouthSouthEast16)
        val south = setOf(South16, South8)
        val southSouthWest = setOf(SouthSouthWest16)
        val southWest = setOf(SouthWest16, SouthWest8)
    }
}
