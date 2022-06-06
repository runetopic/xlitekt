package xlitekt.game.actor.movement

/**
 * @author Jordan Abraham
 */
@JvmInline
value class Direction(
    val packedDirection: Int
) {
    constructor(dx: Int, dz: Int) : this((dx and 0xff shl 8) or (dz and 0xff))

    inline val deltaX get() = (packedDirection shr 8 and 0xff).let { if (it > Byte.MAX_VALUE) it - 0x100 else it }
    inline val deltaZ get() = (packedDirection and 0xff).let { if (it > Byte.MAX_VALUE) it - 0x100 else it }

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
    inline val opcodeForPlayerDirection get() = when (packedDirection) {
        514 -> 15
        258 -> 14
        2 -> 13
        65282 -> 12
        65026 -> 11
        513 -> 10
        65025 -> 9
        512 -> 8
        65024, 257 -> 7
        767, 1 -> 6
        65279, 65281 -> 5
        766, 256 -> 4
        510, 65280 -> 3
        254, 511 -> 2
        65534, 255 -> 1
        65278, 65535 -> 0
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
    inline val opcodeForNPCDirection get() = when (packedDirection) {
        // TODO Running support.
        511 -> 7
        255 -> 6
        65535 -> 5
        256 -> 4
        65280 -> 3
        257 -> 2
        1 -> 1
        65281 -> 0
        else -> throw IllegalStateException("Direction opcode not found for npc. Direction was $this")
    }

    /**
     * Returns the angle associated with this direction.
     */
    inline val angle get() = when (packedDirection) {
        65026, 65281 -> 768
        65282 -> 896
        2, 1 -> 1024
        258 -> 1152
        514, 257 -> 1280
        65025 -> 640
        513 -> 1408
        65024, 65280 -> 512
        512, 256 -> 1536
        65279 -> 384
        767 -> 1664
        65278, 65535 -> 256
        65534 -> 128
        254, 255 -> 2048
        510 -> 1920
        766, 511 -> 1792
        else -> 2048 // Default to south.
    }

    inline val isFourPointCardinal get() = angle % 256 == 0

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
        val north = setOf(North16, North8)
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
