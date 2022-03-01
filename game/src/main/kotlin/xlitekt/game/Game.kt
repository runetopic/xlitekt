package xlitekt.game

import kotlin.experimental.and
import org.rsmod.pathfinder.flag.CollisionFlag.FLOOR
import xlitekt.cache.provider.map.MapEntryTypeProvider
import xlitekt.cache.provider.map.MapSquareEntryType
import xlitekt.game.world.engine.LoopTask
import xlitekt.game.world.map.location.Location
import xlitekt.game.world.map.zone.ZoneFlags
import xlitekt.shared.inject

class Game {
    private val loop = LoopTask()
    private val maps by inject<MapEntryTypeProvider>()

    fun start() {
        loop.start()

        maps.entries().parallelStream().forEach {
            applyCollisionMap(it)
        }
    }

    private fun applyCollisionMap(type: MapSquareEntryType) {
        repeat(MapEntryTypeProvider.LEVELS) { level ->
            repeat(MapEntryTypeProvider.MAP_SIZE) { x ->
                repeat(MapEntryTypeProvider.MAP_SIZE) { z ->
                    run {
                        if ((type.collision[level][x][z] and MapEntryTypeProvider.BLOCKED_TILE_BIT) != MapEntryTypeProvider.BLOCKED_TILE_BIT) return@run

                        val actualLevel = if ((type.collision[1][x][z] and MapEntryTypeProvider.BRIDGE_TILE_BIT) == MapEntryTypeProvider.BRIDGE_TILE_BIT) level - 1 else level

                        if (actualLevel < 0) return@run

                        val baseX = type.regionX shl 6
                        val baseZ = type.regionZ shl 6
                        val location = Location(baseX + x, baseZ + z, actualLevel)
//                        // TODO build zones and set collision there using this location
                        ZoneFlags.add(location.x, location.z, level, FLOOR)
                    }
                }
            }
        }
    }

    fun shutdown() {
        loop.shutdown()
    }
}
