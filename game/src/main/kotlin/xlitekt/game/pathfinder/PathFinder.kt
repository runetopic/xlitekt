package xlitekt.game.pathfinder

import org.rsmod.pathfinder.collision.CollisionStrategies
import org.rsmod.pathfinder.flag.CollisionFlag.BLOCK_EAST
import org.rsmod.pathfinder.flag.CollisionFlag.BLOCK_NORTH
import org.rsmod.pathfinder.flag.CollisionFlag.BLOCK_NORTH_EAST
import org.rsmod.pathfinder.flag.CollisionFlag.BLOCK_NORTH_WEST
import org.rsmod.pathfinder.flag.CollisionFlag.BLOCK_SOUTH
import org.rsmod.pathfinder.flag.CollisionFlag.BLOCK_SOUTH_EAST
import org.rsmod.pathfinder.flag.CollisionFlag.BLOCK_SOUTH_WEST
import org.rsmod.pathfinder.flag.CollisionFlag.BLOCK_WEST
import xlitekt.game.actor.Actor
import xlitekt.game.actor.movement.Direction
import xlitekt.game.world.map.collision.CollisionMap.collisionFlag
import xlitekt.game.world.map.location.Location

private const val SIZE = 128

const val DEFAULT_SRC_SIZE = 1
const val DEFAULT_DEST_WIDTH = 0
const val DEFAULT_DEST_HEIGHT = 0
const val DEFAULT_MAX_TURNS = 24
const val DEFAULT_OBJ_ROT = 10
const val DEFAULT_OBJ_SHAPE = -1
const val DEFAULT_MOVE_NEAR_FLAG = true
const val DEFAULT_ACCESS_BITMASK = 0

fun Actor.canTransverse(location: Location, direction: Direction): Boolean {
    val destination = location // .transform(direction)
    val flag = collisionFlag(destination)
    val strategy = CollisionStrategies.Normal
    return when (direction) {
        Direction.West -> strategy.canMove(flag, BLOCK_WEST)
        Direction.East -> strategy.canMove(flag, BLOCK_EAST)
        Direction.North -> strategy.canMove(flag, BLOCK_NORTH)
        Direction.South -> strategy.canMove(flag, BLOCK_SOUTH)
        Direction.SouthWest ->
            strategy.canMove(flag, BLOCK_SOUTH_WEST) &&
                strategy.canMove(flag, BLOCK_WEST) &&
                strategy.canMove(flag, BLOCK_SOUTH)
        Direction.SouthEast ->
            strategy.canMove(flag, BLOCK_SOUTH_EAST) &&
                strategy.canMove(flag, BLOCK_EAST) &&
                strategy.canMove(flag, BLOCK_SOUTH)
        Direction.NorthWest ->
            strategy.canMove(flag, BLOCK_NORTH_WEST) &&
                strategy.canMove(flag, BLOCK_WEST) &&
                strategy.canMove(flag, BLOCK_NORTH)
        Direction.NorthEast ->
            strategy.canMove(flag, BLOCK_NORTH_EAST) &&
                strategy.canMove(flag, BLOCK_EAST) &&
                strategy.canMove(flag, BLOCK_NORTH)
        else -> TODO()
    }
}
