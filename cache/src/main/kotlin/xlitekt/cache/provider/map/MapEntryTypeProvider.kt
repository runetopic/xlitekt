package xlitekt.cache.provider.map

import com.github.michaelbull.logging.InlineLogger
import com.runetopic.cache.codec.decompress
import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readUByte
import xlitekt.cache.provider.EntryTypeProvider
import xlitekt.shared.buffer.readIncrSmallSmart
import xlitekt.shared.buffer.readUShortSmart
import xlitekt.shared.inject
import xlitekt.shared.resource.MapSquares
import java.util.Collections
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import kotlin.experimental.and
import kotlin.time.measureTime

/**
 * @author Tyler Telis
 */
class MapEntryTypeProvider : EntryTypeProvider<MapSquareEntryType>() {
    private val logger = InlineLogger()
    private val latch = CountDownLatch(VALID_X * VALID_Z)
    private val pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() - 2)
    private val xteas by inject<MapSquares>()

    override fun load(): Map<Int, MapSquareEntryType> {
        val mapSquares = Collections.synchronizedMap<Int, MapSquareEntryType>(mutableMapOf())
        var count = 0
        var missingXteasCount = 0

        val index = store.index(MAP_INDEX)

        val time = measureTime {
            repeat(VALID_X) { x ->
                repeat(VALID_Z) { z ->
                    val regionId = (x shl 8) or z

                    pool.execute {
                        val mapData = index.group("m${x}_$z").data
                        val locData = index.group("l${x}_$z").data

                        if (mapData.isEmpty() || locData.isEmpty()) {
                            latch.countDown()
                            return@execute
                        }

                        val xteas = xteas.find { it.mapsquare == regionId }?.key?.toIntArray() ?: run {
                            latch.countDown()
                            missingXteasCount++
                            return@execute
                        }

                        try {
                            val mapSquare = ByteReadPacket(mapData.decompress()).loadEntryType(MapSquareEntryType(regionId, x, z))
                            ByteReadPacket(locData.decompress(xteas)).loadMapEntryLocations(mapSquare)
                            mapSquares[regionId] = mapSquare
                        } catch (exception: Exception) {
                            logger.error(exception) { "Failed to decompress maps." }
                        } finally {
                            count++
                            latch.countDown()
                        }
                    }
                }
            }
        }
        latch.await()
        pool.shutdown()
        logger.debug { "Took $time to finish. Loaded $count maps. (Missing $missingXteasCount.)" }
        return mapSquares
    }

    override fun ByteReadPacket.loadEntryType(type: MapSquareEntryType): MapSquareEntryType {
        repeat(LEVELS) { level ->
            repeat(MAP_SIZE) { x ->
                repeat(MAP_SIZE) { z ->
                    decodeCollision(type, level, x, z)
                }
            }
        }

        applyCollisionMap(type)
        return type
    }

    private fun applyCollisionMap(type: MapSquareEntryType) {
        repeat(LEVELS) { level ->
            repeat(MAP_SIZE) { x ->
                repeat(MAP_SIZE) { z ->
                    run {
                        if ((type.collision[level][x][z] and BLOCKED_TILE_BIT) != BLOCKED_TILE_BIT) return@run

                        val actualLevel = if ((type.collision[1][x][z] and BRIDGE_TILE_BIT) == BRIDGE_TILE_BIT) level - 1 else level

                        if (actualLevel < 0) return@run

                        val baseX = type.regionX shl 6
                        val baseZ = type.regionZ shl 6
//                        val location = Location(baseX + x, baseZ + z, actualLevel)
//                        // TODO build zones and set collision there using this location
//                        ZoneFlags.add(location.x, location.z, level, FLOOR)
                    }
                }
            }
        }
    }

    private tailrec fun ByteReadPacket.decodeCollision(
        map: MapSquareEntryType,
        level: Int,
        x: Int,
        z: Int
    ) {
        when (val opcode = readUByte().toInt()) {
            0 -> return
            1 -> { // Tile height
                discard(1)
                return
            }
            in 50..81 -> map.collision[level][x][z] = ((opcode - 49).toByte())
        }

        return decodeCollision(map, level, x, z)
    }

    private fun ByteReadPacket.loadMapEntryLocations(type: MapSquareEntryType) {
        var objectId = -1

        while (canRead()) {
            val offset = readIncrSmallSmart()

            if (offset == 0) return

            var packedCoordinates = 0

            objectId += offset

            while (canRead()) {
                val diff = readUShortSmart()

                if (diff == 0) return

                packedCoordinates += diff - 1
                val attributes = readUByte().toInt()
                val localX = (packedCoordinates shr 6) and 0x3f
                val localZ = packedCoordinates and 0x3f

                val shape = attributes shr 2
                val rotation = attributes and 0x3
                var level = (attributes shr 12) and 0x3

                if ((type.collision[level][localX][localZ] and BLOCKED_TILE_BIT) == BLOCKED_TILE_BIT) {
                    level--
                }

                if (level < 0) return

                val baseX = type.regionX shl 6
                val baseZ = type.regionZ shl 6
//                val location = Location(baseX + localX, baseZ + localZ, level)
//                val entry = entryType(objectId) ?: return logger.debug { "LocEntryType was not found. Ignoring objectId=$objectId" }
//                val gameObject = GameObject(entry, location, shape, rotation)
//                CollisionMap.addObjectCollision(gameObject)

                // TODO actually spawn the object in a list for operations
            }
        }
    }

    companion object {
        const val BLOCKED_TILE_BIT = 0x1.toByte()
        const val BRIDGE_TILE_BIT = 0x2.toByte()

        const val VALID_X = 100
        const val VALID_Z = 256

        const val LEVELS = 4
        const val MAP_SIZE = 64
    }
}
