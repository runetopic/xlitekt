package com.runetopic.xlitekt.game.world.map.collision

import com.runetopic.xlitekt.game.world.map.location.Location
import com.runetopic.xlitekt.game.world.map.obj.GameObject
import com.runetopic.xlitekt.game.world.map.obj.GameObjectShape
import com.runetopic.xlitekt.game.world.map.zone.ZoneFlags
import org.rsmod.pathfinder.flag.CollisionFlag.FLOOR_DECORATION
import org.rsmod.pathfinder.flag.CollisionFlag.OBJECT
import org.rsmod.pathfinder.flag.CollisionFlag.OBJECT_PROJECTILE_BLOCKER
import org.rsmod.pathfinder.flag.CollisionFlag.OBJECT_ROUTE_BLOCKER
import org.rsmod.pathfinder.flag.CollisionFlag.WALL_EAST
import org.rsmod.pathfinder.flag.CollisionFlag.WALL_EAST_PROJECTILE_BLOCKER
import org.rsmod.pathfinder.flag.CollisionFlag.WALL_EAST_ROUTE_BLOCKER
import org.rsmod.pathfinder.flag.CollisionFlag.WALL_NORTH
import org.rsmod.pathfinder.flag.CollisionFlag.WALL_NORTH_EAST
import org.rsmod.pathfinder.flag.CollisionFlag.WALL_NORTH_EAST_PROJECTILE_BLOCKER
import org.rsmod.pathfinder.flag.CollisionFlag.WALL_NORTH_EAST_ROUTE_BLOCKER
import org.rsmod.pathfinder.flag.CollisionFlag.WALL_NORTH_PROJECTILE_BLOCKER
import org.rsmod.pathfinder.flag.CollisionFlag.WALL_NORTH_ROUTE_BLOCKER
import org.rsmod.pathfinder.flag.CollisionFlag.WALL_NORTH_WEST
import org.rsmod.pathfinder.flag.CollisionFlag.WALL_NORTH_WEST_PROJECTILE_BLOCKER
import org.rsmod.pathfinder.flag.CollisionFlag.WALL_NORTH_WEST_ROUTE_BLOCKER
import org.rsmod.pathfinder.flag.CollisionFlag.WALL_SOUTH
import org.rsmod.pathfinder.flag.CollisionFlag.WALL_SOUTH_EAST
import org.rsmod.pathfinder.flag.CollisionFlag.WALL_SOUTH_EAST_PROJECTILE_BLOCKER
import org.rsmod.pathfinder.flag.CollisionFlag.WALL_SOUTH_EAST_ROUTE_BLOCKER
import org.rsmod.pathfinder.flag.CollisionFlag.WALL_SOUTH_PROJECTILE_BLOCKER
import org.rsmod.pathfinder.flag.CollisionFlag.WALL_SOUTH_ROUTE_BLOCKER
import org.rsmod.pathfinder.flag.CollisionFlag.WALL_SOUTH_WEST
import org.rsmod.pathfinder.flag.CollisionFlag.WALL_SOUTH_WEST_PROJECTILE_BLOCKER
import org.rsmod.pathfinder.flag.CollisionFlag.WALL_SOUTH_WEST_ROUTE_BLOCKER
import org.rsmod.pathfinder.flag.CollisionFlag.WALL_WEST
import org.rsmod.pathfinder.flag.CollisionFlag.WALL_WEST_PROJECTILE_BLOCKER
import org.rsmod.pathfinder.flag.CollisionFlag.WALL_WEST_ROUTE_BLOCKER

object CollisionMap {
    fun collisionFlag(location: Location) = ZoneFlags[location]
    fun addObjectCollision(obj: GameObject) = changeCollision(obj, true)
    fun removeObjectCollision(obj: GameObject) = changeCollision(obj, false)

    private fun changeCollision(obj: GameObject, add: Boolean) {
        val entry = obj.entry
        val shape = obj.shape
        val location = obj.location
        val rotation = obj.rotation
        val clipType = entry.clipType
        val blockProjectile = entry.blockProjectile
        val breakRouteFinding = entry.breakRouteFinding

        when {
            shape in GameObjectShape.WALL_SHAPES && clipType != 0 -> changeWallCollision(
                location,
                rotation,
                shape,
                blockProjectile,
                !breakRouteFinding,
                add
            )
            shape in GameObjectShape.NORMAL_SHAPES && clipType != 0 -> {
                var width = entry.width
                var length = entry.height
                if (rotation == 1 || rotation == 3) {
                    width = entry.height
                    length = entry.width
                }
                changeCollision(location, width, length, blockProjectile, !breakRouteFinding, add)
            }
            shape in GameObjectShape.GROUND_DECOR_SHAPES && clipType == 1 -> changeFloorDecor(location, add)
        }
    }

    private fun changeCollision(
        location: Location,
        width: Int,
        length: Int,
        blocksProjectile: Boolean,
        breakRouteFinding: Boolean,
        add: Boolean
    ) {
        var flag = OBJECT

        if (blocksProjectile) {
            flag = flag or OBJECT_PROJECTILE_BLOCKER
        }

        if (breakRouteFinding) {
            flag = flag or OBJECT_ROUTE_BLOCKER
        }

        for (x in 0 until width) {
            for (y in 0 until length) {
                val translate = location.transform(x, y)
                addCollisionFlag(translate, flag, add)
            }
        }
    }

    private fun changeWallRouteFinding(
        location: Location,
        rotation: Int,
        shape: Int,
        add: Boolean
    ) {
        when (shape) {
            GameObjectShape.WALL -> {
                when (rotation) {
                    0 -> {
                        addCollisionFlag(location, WALL_WEST_ROUTE_BLOCKER, add)
                        addCollisionFlag(location.transform(-1, 0), WALL_EAST_ROUTE_BLOCKER, add)
                    }
                    1 -> {
                        addCollisionFlag(location, WALL_NORTH_ROUTE_BLOCKER, add)
                        addCollisionFlag(location.transform(0, 1), WALL_SOUTH_ROUTE_BLOCKER, add)
                    }
                    2 -> {
                        addCollisionFlag(location, WALL_EAST_ROUTE_BLOCKER, add)
                        addCollisionFlag(location.transform(1, 0), WALL_WEST_ROUTE_BLOCKER, add)
                    }
                    3 -> {
                        addCollisionFlag(location, WALL_SOUTH_ROUTE_BLOCKER, add)
                        addCollisionFlag(location.transform(0, -1), WALL_NORTH_ROUTE_BLOCKER, add)
                    }
                }
            }
            GameObjectShape.WALL_CORNER_DIAG, GameObjectShape.WALL_CORNER -> {
                when (rotation) {
                    0 -> {
                        addCollisionFlag(location, WALL_NORTH_WEST_ROUTE_BLOCKER, add)
                        addCollisionFlag(location.transform(-1, 1), WALL_SOUTH_EAST_ROUTE_BLOCKER, add)
                    }
                    1 -> {
                        addCollisionFlag(location, WALL_NORTH_EAST_ROUTE_BLOCKER, add)
                        addCollisionFlag(location.transform(1, 1), WALL_SOUTH_WEST_ROUTE_BLOCKER, add)
                    }
                    2 -> {
                        addCollisionFlag(location, WALL_SOUTH_EAST_ROUTE_BLOCKER, add)
                        addCollisionFlag(location.transform(1, -1), WALL_NORTH_WEST_ROUTE_BLOCKER, add)
                    }
                    3 -> {
                        addCollisionFlag(location, WALL_SOUTH_WEST_ROUTE_BLOCKER, add)
                        addCollisionFlag(location.transform(-1, -1), WALL_NORTH_EAST_ROUTE_BLOCKER, add)
                    }
                }
            }
            GameObjectShape.UNFINISHED_WALL -> {
                when (rotation) {
                    0 -> {
                        addCollisionFlag(location, WALL_NORTH_ROUTE_BLOCKER or WALL_WEST_ROUTE_BLOCKER, add)
                        addCollisionFlag(location.transform(-1, 0), WALL_EAST_ROUTE_BLOCKER, add)
                        addCollisionFlag(location.transform(0, 1), WALL_SOUTH_ROUTE_BLOCKER, add)
                    }
                    1 -> {
                        addCollisionFlag(location, WALL_NORTH_ROUTE_BLOCKER or WALL_EAST_ROUTE_BLOCKER, add)
                        addCollisionFlag(location.transform(0, 1), WALL_SOUTH_ROUTE_BLOCKER, add)
                        addCollisionFlag(location.transform(1, 0), WALL_WEST_ROUTE_BLOCKER, add)
                    }
                    2 -> {
                        addCollisionFlag(location, WALL_SOUTH_ROUTE_BLOCKER or WALL_EAST_ROUTE_BLOCKER, add)
                        addCollisionFlag(location.transform(1, 0), WALL_WEST_ROUTE_BLOCKER, add)
                        addCollisionFlag(location.transform(0, -1), WALL_NORTH_ROUTE_BLOCKER, add)
                    }
                    3 -> {
                        addCollisionFlag(location, WALL_SOUTH_ROUTE_BLOCKER or WALL_WEST_ROUTE_BLOCKER, add)
                        addCollisionFlag(location.transform(0, -1), WALL_NORTH_ROUTE_BLOCKER, add)
                        addCollisionFlag(location.transform(-1, 0), WALL_EAST_ROUTE_BLOCKER, add)
                    }
                }
            }
        }
    }

    private fun changeWallCollision(
        location: Location,
        rotation: Int,
        shape: Int,
        blockProjectile: Boolean,
        breakRouteFinding: Boolean,
        add: Boolean
    ) {
        changeWallCollision(location, rotation, shape, add)
        if (blockProjectile) changeWallProjectileCollision(location, rotation, shape, add)
        if (breakRouteFinding) changeWallRouteFinding(location, rotation, shape, add)
    }

    private fun changeWallProjectileCollision(location: Location, rotation: Int, shape: Int, add: Boolean) {
        when (shape) {
            GameObjectShape.WALL -> {
                when (rotation) {
                    0 -> {
                        addCollisionFlag(location, WALL_WEST_PROJECTILE_BLOCKER, add)
                        addCollisionFlag(location.transform(-1, 0), WALL_EAST_PROJECTILE_BLOCKER, add)
                    }
                    1 -> {
                        addCollisionFlag(location, WALL_NORTH_PROJECTILE_BLOCKER, add)
                        addCollisionFlag(location.transform(0, 1), WALL_SOUTH_PROJECTILE_BLOCKER, add)
                    }
                    2 -> {
                        addCollisionFlag(location, WALL_EAST_PROJECTILE_BLOCKER, add)
                        addCollisionFlag(location.transform(1, 0), WALL_WEST_PROJECTILE_BLOCKER, add)
                    }
                    3 -> {
                        addCollisionFlag(location, WALL_SOUTH_PROJECTILE_BLOCKER, add)
                        addCollisionFlag(location.transform(0, -1), WALL_NORTH_PROJECTILE_BLOCKER, add)
                    }
                }
            }
            GameObjectShape.WALL_CORNER_DIAG, GameObjectShape.WALL_CORNER -> {
                when (rotation) {
                    0 -> {
                        addCollisionFlag(location, WALL_NORTH_WEST_PROJECTILE_BLOCKER, add)
                        addCollisionFlag(location.transform(-1, 1), WALL_SOUTH_EAST_PROJECTILE_BLOCKER, add)
                    }
                    1 -> {
                        addCollisionFlag(location, WALL_NORTH_EAST_PROJECTILE_BLOCKER, add)
                        addCollisionFlag(location.transform(1, 1), WALL_SOUTH_WEST_PROJECTILE_BLOCKER, add)
                    }
                    2 -> {
                        addCollisionFlag(location, WALL_SOUTH_EAST_PROJECTILE_BLOCKER, add)
                        addCollisionFlag(location.transform(1, -1), WALL_NORTH_WEST_PROJECTILE_BLOCKER, add)
                    }
                    3 -> {
                        addCollisionFlag(location, WALL_SOUTH_WEST_PROJECTILE_BLOCKER, add)
                        addCollisionFlag(location.transform(-1, -1), WALL_NORTH_EAST_PROJECTILE_BLOCKER, add)
                    }
                }
            }
            GameObjectShape.UNFINISHED_WALL -> {
                when (rotation) {
                    0 -> {
                        val flag = WALL_WEST_PROJECTILE_BLOCKER or WALL_NORTH_PROJECTILE_BLOCKER
                        addCollisionFlag(location, flag, add)
                        addCollisionFlag(location.transform(-1, 0), WALL_EAST_PROJECTILE_BLOCKER, add)
                        addCollisionFlag(location.transform(0, 1), WALL_SOUTH_PROJECTILE_BLOCKER, add)
                    }
                    1 -> {
                        val flag = WALL_NORTH_PROJECTILE_BLOCKER or
                            WALL_EAST_PROJECTILE_BLOCKER
                        addCollisionFlag(location, flag, add)
                        addCollisionFlag(location.transform(0, 1), WALL_SOUTH_PROJECTILE_BLOCKER, add)
                        addCollisionFlag(location.transform(1, 0), WALL_WEST_PROJECTILE_BLOCKER, add)
                    }
                    2 -> {
                        val flag = WALL_EAST_PROJECTILE_BLOCKER or
                            WALL_SOUTH_PROJECTILE_BLOCKER
                        addCollisionFlag(location, flag, add)
                        addCollisionFlag(location.transform(1, 0), WALL_WEST_PROJECTILE_BLOCKER, add)
                        addCollisionFlag(location.transform(0, -1), WALL_NORTH_PROJECTILE_BLOCKER, add)
                    }
                    3 -> {
                        val flag = WALL_SOUTH_PROJECTILE_BLOCKER or WALL_WEST_PROJECTILE_BLOCKER
                        addCollisionFlag(location, flag, add)
                        addCollisionFlag(location.transform(0, -1), WALL_NORTH_PROJECTILE_BLOCKER, add)
                        addCollisionFlag(location.transform(-1, 0), WALL_EAST_PROJECTILE_BLOCKER, add)
                    }
                }
            }
        }
    }

    private fun changeWallCollision(
        location: Location,
        rotation: Int,
        shape: Int,
        add: Boolean
    ) {
        when (shape) {
            GameObjectShape.WALL -> {
                when (rotation) {
                    0 -> {
                        addCollisionFlag(location, WALL_WEST, add)
                        addCollisionFlag(location.transform(-1, 0), WALL_EAST, add)
                    }
                    1 -> {
                        addCollisionFlag(location, WALL_NORTH, add)
                        addCollisionFlag(location.transform(0, 1), WALL_SOUTH, add)
                    }
                    2 -> {
                        addCollisionFlag(location, WALL_EAST, add)
                        addCollisionFlag(location.transform(1, 0), WALL_WEST, add)
                    }
                    3 -> {
                        addCollisionFlag(location, WALL_SOUTH, add)
                        addCollisionFlag(location.transform(0, -1), WALL_NORTH, add)
                    }
                }
            }
            GameObjectShape.WALL_CORNER_DIAG, GameObjectShape.WALL_CORNER -> {
                when (rotation) {
                    0 -> {
                        addCollisionFlag(location, WALL_NORTH_WEST, add)
                        addCollisionFlag(location.transform(-1, 1), WALL_SOUTH_EAST, add)
                    }
                    1 -> {
                        addCollisionFlag(location, WALL_NORTH_EAST, add)
                        addCollisionFlag(location.transform(1, 1), WALL_SOUTH_WEST, add)
                    }
                    2 -> {
                        addCollisionFlag(location, WALL_SOUTH_EAST, add)
                        addCollisionFlag(location.transform(1, -1), WALL_NORTH_WEST, add)
                    }
                    3 -> {
                        addCollisionFlag(location, WALL_SOUTH_WEST, add)
                        addCollisionFlag(location.transform(-1, -1), WALL_NORTH_EAST, add)
                    }
                }
            }
            GameObjectShape.UNFINISHED_WALL -> {
                when (rotation) {
                    0 -> {
                        addCollisionFlag(location, WALL_WEST or WALL_NORTH, add)
                        addCollisionFlag(location.transform(-1, 0), WALL_EAST, add)
                        addCollisionFlag(location.transform(0, 1), WALL_SOUTH, add)
                    }
                    1 -> {
                        addCollisionFlag(location, WALL_NORTH or WALL_EAST, add)
                        addCollisionFlag(location.transform(0, 1), WALL_SOUTH, add)
                        addCollisionFlag(location.transform(1, 0), WALL_WEST, add)
                    }
                    2 -> {
                        addCollisionFlag(location, WALL_EAST or WALL_SOUTH, add)
                        addCollisionFlag(location.transform(1, 0), WALL_WEST, add)
                        addCollisionFlag(location.transform(0, -1), WALL_NORTH, add)
                    }
                    3 -> {
                        addCollisionFlag(location, WALL_SOUTH or WALL_WEST, add)
                        addCollisionFlag(location.transform(0, -1), WALL_NORTH, add)
                        addCollisionFlag(location.transform(-1, 0), WALL_EAST, add)
                    }
                }
            }
        }
    }

    private fun changeFloorDecor(coords: Location, add: Boolean) = addCollisionFlag(coords, FLOOR_DECORATION, add)

    private fun addCollisionFlag(coords: Location, mask: Int, add: Boolean) = when {
        add -> ZoneFlags.add(coords, mask)
        else -> ZoneFlags.remove(coords, mask)
    }
}
