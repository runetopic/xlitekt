package xlitekt.game

import com.github.michaelbull.logging.InlineLogger
import kotlin.experimental.and
import xlitekt.cache.provider.config.loc.LocEntryTypeProvider
import xlitekt.cache.provider.map.MapEntryTypeProvider
import xlitekt.cache.provider.map.MapEntryTypeProvider.Companion.BLOCKED_TILE_BIT
import xlitekt.cache.provider.map.MapEntryTypeProvider.Companion.BRIDGE_TILE_BIT
import xlitekt.cache.provider.map.MapEntryTypeProvider.Companion.LEVELS
import xlitekt.cache.provider.map.MapEntryTypeProvider.Companion.MAP_SIZE
import xlitekt.cache.provider.map.MapSquareEntryType
import xlitekt.game.world.engine.LoopTask
import xlitekt.game.world.map.collision.CollisionMap
import xlitekt.game.world.map.location.Location
import xlitekt.game.world.map.obj.GameObject
import xlitekt.shared.inject

class Game {
    private val loop = LoopTask()
    private val maps by inject<MapEntryTypeProvider>()
    private val locs by inject<LocEntryTypeProvider>()
    private val logger = InlineLogger()

    fun start() {
        loop.start()

        maps.entries().parallelStream().forEach {
            applyCollisionMap(it)
        }
    }

    private fun applyCollisionMap(type: MapSquareEntryType) {
        for (level in 0 until LEVELS) {
            for (x in 0 until MAP_SIZE) {
                for (z in 0 until MAP_SIZE) {
                    if ((type.collision[level][x][z] and BLOCKED_TILE_BIT) != BLOCKED_TILE_BIT) continue

                    val actualLevel = if ((type.collision[1][x][z] and BRIDGE_TILE_BIT) == BRIDGE_TILE_BIT) level - 1 else level

                    if (actualLevel < 0) continue

                    val baseX = type.regionX shl 6
                    val baseZ = type.regionZ shl 6
                    val location = Location(x + baseX, z + baseZ, level)
                    CollisionMap.addFloorCollision(location)
                }
            }
        }

        for (level in 0 until LEVELS) {
            for (x in 0 until MAP_SIZE) {
                for (z in 0 until MAP_SIZE) {
                    for (mapLocation in type.locations[level][x][z]) {
                        val entry = locs.entryType(mapLocation.id) ?: continue
                        val baseX = type.regionX shl 6
                        val baseZ = type.regionZ shl 6
                        val location = Location(x + baseX, z + baseZ, mapLocation.level)
                        val gameObject = GameObject(entry, location, mapLocation.shape, mapLocation.rotation)
                        CollisionMap.addObjectCollision(gameObject)
                    }
                }
            }
        }
    }

    fun shutdown() {
        loop.shutdown()
    }
}
