package com.runetopic.xlitekt.game.pathfinder

import com.runetopic.xlitekt.game.actor.Actor
import com.runetopic.xlitekt.game.actor.movement.Direction
import com.runetopic.xlitekt.game.world.map.collision.CollisionMap.collisionFlag
import com.runetopic.xlitekt.game.world.map.location.Location
import com.runetopic.xlitekt.game.world.map.zone.ZoneFlags
import org.rsmod.pathfinder.SmartPathFinder
import org.rsmod.pathfinder.collision.CollisionStrategies
import org.rsmod.pathfinder.flag.CollisionFlag.BLOCK_EAST
import org.rsmod.pathfinder.flag.CollisionFlag.BLOCK_NORTH
import org.rsmod.pathfinder.flag.CollisionFlag.BLOCK_NORTH_EAST
import org.rsmod.pathfinder.flag.CollisionFlag.BLOCK_NORTH_WEST
import org.rsmod.pathfinder.flag.CollisionFlag.BLOCK_SOUTH
import org.rsmod.pathfinder.flag.CollisionFlag.BLOCK_SOUTH_EAST
import org.rsmod.pathfinder.flag.CollisionFlag.BLOCK_SOUTH_WEST
import org.rsmod.pathfinder.flag.CollisionFlag.BLOCK_WEST

private const val SIZE = 128

// TODO: when everything is working multi thread pathfinding potentially (might not be worth it)
val pathFinder = SmartPathFinder(
    searchMapSize = SIZE,
    useRouteBlockerFlags = true,
    flags = ZoneFlags.flags,
    defaultFlag = 0
)

fun Actor.canTransverse(location: Location, direction: Direction): Boolean {
    val destination = location.transform(direction)
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
    }
}
