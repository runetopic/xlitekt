package com.runetopic.xlitekt.game.collision

import com.runetopic.xlitekt.game.location.Location
import com.runetopic.xlitekt.game.obj.GameObject
import com.runetopic.xlitekt.game.obj.GameObjectShape
import com.runetopic.xlitekt.game.zone.ZoneFlags

object CollisionMap {
    fun addObjectCollision(obj: GameObject) = changeCollision(obj, true)
    fun removeObjectCollision(obj: GameObject) = changeCollision(obj, false)

    private fun changeCollision(obj: GameObject, add: Boolean) {
        val entry = obj.entry
        val shape = obj.shape
        val coords = obj.location
        val rotation = obj.rotation
        val clipType = entry.clipType
        val blockPath = entry.blockPath
        val blockProjectile = entry.blockProjectile

        when {
            shape in GameObjectShape.WALL_SHAPES && clipType != 0 -> {
                changeWallCollision(coords, rotation, shape, blockProjectile, add)
            }
            shape in GameObjectShape.NORMAL_SHAPES && clipType != 0 -> {
                var width = entry.width
                var length = entry.height
                if (rotation == 1 || rotation == 3) {
                    width = entry.height
                    length = entry.width
                }
                changeNormal(coords, width, length, blockPath, blockProjectile, add)
            }
            shape in GameObjectShape.GROUND_DECOR_SHAPES && clipType == 1 -> {
                changeFloorDecor(coords, add)
            }
        }
    }

    private fun changeNormal(
        coords: Location,
        width: Int,
        length: Int,
        blockPath: Boolean,
        blockProjectile: Boolean,
        add: Boolean
    ) {
        for (x in 0 until width) {
            for (y in 0 until length) {
                val translate = coords.transform(x, y)
                addCollisionFlag(translate, CollisionFlag.OBJECT, add)
                if (blockProjectile) {
                    addCollisionFlag(translate, CollisionFlag.OBJECT_PROJECTILE_BLOCKER, add)
                }
                if (blockPath) {
                    addCollisionFlag(translate, CollisionFlag.OBJECT_ROUTE_BLOCKER, add)
                }
            }
        }
    }

    private fun changeWallCollision(
        location: Location,
        rotation: Int,
        shape: Int,
        blockProjectile: Boolean,
        add: Boolean
    ) {
        when (shape) {
            GameObjectShape.WALL -> {
                when (rotation) {
                    0 -> {
                        addCollisionFlag(location, CollisionFlag.WALL_WEST, add)
                        addCollisionFlag(location.transform(-1, 0), CollisionFlag.WALL_EAST, add)
                        if (blockProjectile) {
                            addCollisionFlag(location, CollisionFlag.WALL_WEST_PROJECTILE_BLOCKER, add)
                            addCollisionFlag(location.transform(-1, 0), CollisionFlag.WALL_EAST_PROJECTILE_BLOCKER, add)
                        }
                    }
                    1 -> {
                        addCollisionFlag(location, CollisionFlag.WALL_NORTH, add)
                        addCollisionFlag(location.transform(0, 1), CollisionFlag.WALL_SOUTH, add)
                        if (blockProjectile) {
                            addCollisionFlag(location, CollisionFlag.WALL_NORTH_PROJECTILE_BLOCKER, add)
                            addCollisionFlag(location.transform(0, 1), CollisionFlag.WALL_SOUTH_PROJECTILE_BLOCKER, add)
                        }
                    }
                    2 -> {
                        addCollisionFlag(location, CollisionFlag.WALL_EAST, add)
                        addCollisionFlag(location.transform(1, 0), CollisionFlag.WALL_WEST, add)
                        if (blockProjectile) {
                            addCollisionFlag(location, CollisionFlag.WALL_EAST_PROJECTILE_BLOCKER, add)
                            addCollisionFlag(location.transform(1, 0), CollisionFlag.WALL_WEST_PROJECTILE_BLOCKER, add)
                        }
                    }
                    3 -> {
                        addCollisionFlag(location, CollisionFlag.WALL_SOUTH, add)
                        addCollisionFlag(location.transform(0, -1), CollisionFlag.WALL_NORTH, add)
                        if (blockProjectile) {
                            addCollisionFlag(location, CollisionFlag.WALL_SOUTH_PROJECTILE_BLOCKER, add)
                            addCollisionFlag(location.transform(0, -1), CollisionFlag.WALL_NORTH_PROJECTILE_BLOCKER, add)
                        }
                    }
                }
            }
            GameObjectShape.WALL_CORNER_DIAG, GameObjectShape.WALL_CORNER -> {
                when (rotation) {
                    0 -> {
                        addCollisionFlag(location, CollisionFlag.WALL_NORTH_WEST, add)
                        addCollisionFlag(location.transform(-1, 1), CollisionFlag.WALL_SOUTH_EAST, add)
                        if (blockProjectile) {
                            addCollisionFlag(location, CollisionFlag.WALL_NORTH_WEST_PROJECTILE_BLOCKER, add)
                            addCollisionFlag(location.transform(-1, 1), CollisionFlag.WALL_SOUTH_EAST_PROJECTILE_BLOCKER, add)
                        }
                    }
                    1 -> {
                        addCollisionFlag(location, CollisionFlag.WALL_NORTH_EAST, add)
                        addCollisionFlag(location.transform(1, 1), CollisionFlag.WALL_SOUTH_WEST, add)
                        if (blockProjectile) {
                            addCollisionFlag(location, CollisionFlag.WALL_NORTH_EAST_PROJECTILE_BLOCKER, add)
                            addCollisionFlag(location.transform(1, 1), CollisionFlag.WALL_SOUTH_WEST_PROJECTILE_BLOCKER, add)
                        }
                    }
                    2 -> {
                        addCollisionFlag(location, CollisionFlag.WALL_SOUTH_EAST, add)
                        addCollisionFlag(location.transform(1, -1), CollisionFlag.WALL_NORTH_WEST, add)
                        if (blockProjectile) {
                            addCollisionFlag(location, CollisionFlag.WALL_SOUTH_EAST_PROJECTILE_BLOCKER, add)
                            addCollisionFlag(location.transform(1, -1), CollisionFlag.WALL_NORTH_WEST_PROJECTILE_BLOCKER, add)
                        }
                    }
                    3 -> {
                        addCollisionFlag(location, CollisionFlag.WALL_SOUTH_WEST, add)
                        addCollisionFlag(location.transform(-1, -1), CollisionFlag.WALL_NORTH_EAST, add)
                        if (blockProjectile) {
                            addCollisionFlag(location, CollisionFlag.WALL_SOUTH_WEST_PROJECTILE_BLOCKER, add)
                            addCollisionFlag(location.transform(-1, -1), CollisionFlag.WALL_NORTH_EAST_PROJECTILE_BLOCKER, add)
                        }
                    }
                }
            }
            GameObjectShape.UNFINISHED_WALL -> {
                when (rotation) {
                    0 -> {
                        addCollisionFlag(location, CollisionFlag.WALL_WEST or CollisionFlag.WALL_NORTH, add)
                        addCollisionFlag(location.transform(-1, 0), CollisionFlag.WALL_EAST, add)
                        addCollisionFlag(location.transform(0, 1), CollisionFlag.WALL_SOUTH, add)
                        if (blockProjectile) {
                            val flag = CollisionFlag.WALL_WEST_PROJECTILE_BLOCKER or
                                CollisionFlag.WALL_NORTH_PROJECTILE_BLOCKER
                            addCollisionFlag(location, flag, add)
                            addCollisionFlag(location.transform(-1, 0), CollisionFlag.WALL_EAST_PROJECTILE_BLOCKER, add)
                            addCollisionFlag(location.transform(0, 1), CollisionFlag.WALL_SOUTH_PROJECTILE_BLOCKER, add)
                        }
                    }
                    1 -> {
                        addCollisionFlag(location, CollisionFlag.WALL_NORTH or CollisionFlag.WALL_EAST, add)
                        addCollisionFlag(location.transform(0, 1), CollisionFlag.WALL_SOUTH, add)
                        addCollisionFlag(location.transform(1, 0), CollisionFlag.WALL_WEST, add)
                        if (blockProjectile) {
                            val flag = CollisionFlag.WALL_NORTH_PROJECTILE_BLOCKER or
                                CollisionFlag.WALL_EAST_PROJECTILE_BLOCKER
                            addCollisionFlag(location, flag, add)
                            addCollisionFlag(location.transform(0, 1), CollisionFlag.WALL_SOUTH_PROJECTILE_BLOCKER, add)
                            addCollisionFlag(location.transform(1, 0), CollisionFlag.WALL_WEST_PROJECTILE_BLOCKER, add)
                        }
                    }
                    2 -> {
                        addCollisionFlag(location, CollisionFlag.WALL_EAST or CollisionFlag.WALL_SOUTH, add)
                        addCollisionFlag(location.transform(1, 0), CollisionFlag.WALL_WEST, add)
                        addCollisionFlag(location.transform(0, -1), CollisionFlag.WALL_NORTH, add)
                        if (blockProjectile) {
                            val flag = CollisionFlag.WALL_EAST_PROJECTILE_BLOCKER or
                                CollisionFlag.WALL_SOUTH_PROJECTILE_BLOCKER
                            addCollisionFlag(location, flag, add)
                            addCollisionFlag(location.transform(1, 0), CollisionFlag.WALL_WEST_PROJECTILE_BLOCKER, add)
                            addCollisionFlag(location.transform(0, -1), CollisionFlag.WALL_NORTH_PROJECTILE_BLOCKER, add)
                        }
                    }
                    3 -> {
                        addCollisionFlag(location, CollisionFlag.WALL_SOUTH or CollisionFlag.WALL_WEST, add)
                        addCollisionFlag(location.transform(0, -1), CollisionFlag.WALL_NORTH, add)
                        addCollisionFlag(location.transform(-1, 0), CollisionFlag.WALL_EAST, add)
                        if (blockProjectile) {
                            val flag = CollisionFlag.WALL_SOUTH_PROJECTILE_BLOCKER or CollisionFlag.WALL_WEST_PROJECTILE_BLOCKER
                            addCollisionFlag(location, flag, add)
                            addCollisionFlag(location.transform(0, -1), CollisionFlag.WALL_NORTH_PROJECTILE_BLOCKER, add)
                            addCollisionFlag(location.transform(-1, 0), CollisionFlag.WALL_EAST_PROJECTILE_BLOCKER, add)
                        }
                    }
                }
            }
        }
    }

    private fun changeFloorDecor(coords: Location, add: Boolean) = addCollisionFlag(coords, CollisionFlag.FLOOR_DECORATION, add)

    private fun addCollisionFlag(coords: Location, mask: Int, add: Boolean) = when {
        add -> ZoneFlags.add(coords, mask)
        else -> ZoneFlags.remove(coords, mask)
    }
}
