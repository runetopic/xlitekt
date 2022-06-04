package xlitekt.game.world.map

import org.rsmod.pathfinder.ZoneFlags
import org.rsmod.pathfinder.flag.CollisionFlag.FLOOR
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
import xlitekt.cache.provider.config.loc.LocEntryTypeProvider
import xlitekt.cache.provider.map.MapSquareEntryType
import xlitekt.cache.provider.map.MapSquareEntryTypeProvider
import xlitekt.game.world.World
import xlitekt.shared.inject

object CollisionMap {
    private val locs by inject<LocEntryTypeProvider>()
    private val zoneFlags by inject<ZoneFlags>()
    private val world by inject<World>()

    fun applyCollision(type: MapSquareEntryType) {
        for (level in 0 until MapSquareEntryTypeProvider.LEVELS) {
            for (x in 0 until MapSquareEntryTypeProvider.MAP_SIZE) {
                for (z in 0 until MapSquareEntryTypeProvider.MAP_SIZE) {
                    if ((type.terrain[level][x][z]!!.collision and MapSquareEntryTypeProvider.BLOCKED_TILE_BIT) != MapSquareEntryTypeProvider.BLOCKED_TILE_BIT) continue

                    val actualLevel = if ((type.terrain[1][x][z]!!.collision and MapSquareEntryTypeProvider.BRIDGE_TILE_BIT) == MapSquareEntryTypeProvider.BRIDGE_TILE_BIT) level - 1 else level

                    if (actualLevel < 0) continue

                    val baseX = type.regionX shl 6
                    val baseZ = type.regionZ shl 6

                    val location = Location(x + baseX, z + baseZ, level)
                    addFloorCollision(location)
                    world.createZone(location.toZoneLocation())
                }
            }
        }

        for (level in 0 until MapSquareEntryTypeProvider.LEVELS) {
            for (x in 0 until MapSquareEntryTypeProvider.MAP_SIZE) {
                for (z in 0 until MapSquareEntryTypeProvider.MAP_SIZE) {
                    val baseX = type.regionX shl 6
                    val baseZ = type.regionZ shl 6

                    type.locs[level][x][z].forEach {
                        val location = Location(it.x + baseX, it.z + baseZ, it.level)
                        if (!locs.exists(it.id)) return@forEach
                        val gameObject = GameObject(it.id, location, it.shape, it.rotation, false)
                        addObjectCollision(gameObject)
                        world.createZone(location.toZoneLocation()).locs.add(gameObject)
                    }
                }
            }
        }
    }

    fun collisionFlag(location: Location) = zoneFlags[location.x, location.z, location.level]
    fun addObjectCollision(obj: GameObject) = changeNormalCollision(obj, true)
    fun removeObjectCollision(obj: GameObject) = changeNormalCollision(obj, false)

    private fun changeNormalCollision(obj: GameObject, add: Boolean) {
        val entry = obj.entry
        val shape = obj.shape
        val location = obj.location
        val rotation = obj.rotation
        val interactType = entry.interactType
        val blockProjectile = entry.blockProjectile
        val breakRouteFinding = entry.breakRouteFinding

        when {
            shape in GameObjectShape.WALL_SHAPES && interactType != 0 -> {
                changeWallCollision(
                    location,
                    rotation,
                    shape,
                    blockProjectile,
                    !breakRouteFinding,
                    add
                )
            }
            shape in GameObjectShape.NORMAL_SHAPES && interactType != 0 -> {
                var width = entry.width
                var length = entry.height
                if (rotation == 1 || rotation == 3) {
                    width = entry.height
                    length = entry.width
                }
                changeNormalCollision(location, width, length, blockProjectile, !breakRouteFinding, add)
            }
            shape in GameObjectShape.GROUND_DECOR_SHAPES && interactType == 1 -> changeFloorDecor(location, add)
        }
    }

    private fun changeNormalCollision(
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
            GameObjectShape.WALL_STRAIGHT -> when (rotation) {
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
            GameObjectShape.WALL_DIAGONALCORNER, GameObjectShape.WALL_SQUARECORNER -> when (rotation) {
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
            GameObjectShape.WALL_L -> when (rotation) {
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
            GameObjectShape.WALL_STRAIGHT -> when (rotation) {
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
            GameObjectShape.WALL_DIAGONALCORNER, GameObjectShape.WALL_SQUARECORNER -> when (rotation) {
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
            GameObjectShape.WALL_L -> when (rotation) {
                0 -> {
                    val flag = WALL_WEST_PROJECTILE_BLOCKER or WALL_NORTH_PROJECTILE_BLOCKER
                    addCollisionFlag(location, flag, add)
                    addCollisionFlag(location.transform(-1, 0), WALL_EAST_PROJECTILE_BLOCKER, add)
                    addCollisionFlag(location.transform(0, 1), WALL_SOUTH_PROJECTILE_BLOCKER, add)
                }
                1 -> {
                    val flag = WALL_NORTH_PROJECTILE_BLOCKER or WALL_EAST_PROJECTILE_BLOCKER
                    addCollisionFlag(location, flag, add)
                    addCollisionFlag(location.transform(0, 1), WALL_SOUTH_PROJECTILE_BLOCKER, add)
                    addCollisionFlag(location.transform(1, 0), WALL_WEST_PROJECTILE_BLOCKER, add)
                }
                2 -> {
                    val flag = WALL_EAST_PROJECTILE_BLOCKER or WALL_SOUTH_PROJECTILE_BLOCKER
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

    private fun changeWallCollision(
        location: Location,
        rotation: Int,
        shape: Int,
        add: Boolean
    ) {
        when (shape) {
            GameObjectShape.WALL_STRAIGHT -> when (rotation) {
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
            GameObjectShape.WALL_DIAGONALCORNER, GameObjectShape.WALL_SQUARECORNER -> when (rotation) {
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
            GameObjectShape.WALL_L -> when (rotation) {
                0 -> {
                    addCollisionFlag(location, WALL_NORTH or WALL_WEST, add)
                    addCollisionFlag(location.transform(-1, 0), WALL_EAST, add)
                    addCollisionFlag(location.transform(0, 1), WALL_SOUTH, add)
                }
                1 -> {
                    addCollisionFlag(location, WALL_NORTH or WALL_EAST, add)
                    addCollisionFlag(location.transform(0, 1), WALL_SOUTH, add)
                    addCollisionFlag(location.transform(1, 0), WALL_WEST, add)
                }
                2 -> {
                    addCollisionFlag(location, WALL_SOUTH or WALL_EAST, add)
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

    private fun changeFloorDecor(coords: Location, add: Boolean) = addCollisionFlag(coords, FLOOR_DECORATION, add)

    private fun addCollisionFlag(location: Location, mask: Int, add: Boolean) = when {
        add -> zoneFlags.add(location.x, location.z, location.level, mask)
        else -> zoneFlags.remove(location.x, location.z, location.level, mask)
    }

    private fun addFloorCollision(location: Location) = addCollisionFlag(location, FLOOR, true)
}
