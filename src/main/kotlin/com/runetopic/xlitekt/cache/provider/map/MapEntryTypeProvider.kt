package com.runetopic.xlitekt.cache.provider.map

import com.github.michaelbull.logging.InlineLogger
import com.runetopic.cache.codec.decompress
import com.runetopic.xlitekt.cache.provider.EntryTypeProvider
import com.runetopic.xlitekt.game.collision.CollisionFlag.FLOOR
import com.runetopic.xlitekt.game.location.Location
import com.runetopic.xlitekt.game.zone.ZoneFlags
import com.runetopic.xlitekt.plugin.koin.inject
import com.runetopic.xlitekt.shared.buffer.readIncrSmallSmart
import com.runetopic.xlitekt.shared.buffer.readUShortSmart
import com.runetopic.xlitekt.shared.resource.MapSquares
import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readUByte
import java.util.Collections
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import kotlin.experimental.and
import kotlin.system.measureTimeMillis

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

        val time = measureTimeMillis {
            val index = store.index(MAP_INDEX)

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

                        val xteas = xteas.find { it.regionId == regionId }?.keys?.toIntArray() ?: run {
                            latch.countDown()
                            missingXteasCount++
                            return@execute
                        }

                        val mapSquare = ByteReadPacket(mapData.decompress()).loadEntryType(MapSquareEntryType(regionId, x, z))
                        val locations = ByteReadPacket(locData.decompress(xteas)).loadMapEntryLocations(mapSquare)

                        mapSquares[regionId] = mapSquare
                        count++
                        latch.countDown()
                    }
                }
            }
        }
        latch.await()
        pool.shutdown()
        logger.debug { "Finished loading $count maps in $time ms. $missingXteasCount were missing xteas." }
        return mapSquares
    }

    override fun ByteReadPacket.loadEntryType(type: MapSquareEntryType): MapSquareEntryType {
        repeat(PLANES) { plane ->
            repeat(SIZE) { x ->
                repeat(SIZE) { z ->
                    decodeCollision(type, plane, x, z)
                }
            }
        }

        applyCollisionMap(type)
        return type
    }

    private fun applyCollisionMap(type: MapSquareEntryType) {
        repeat(PLANES) { plane ->
            repeat(SIZE) { x ->
                repeat(SIZE) { z ->
                    run {
                        if ((type.collision[plane][x][z] and BLOCKED_TILE_BIT) != BLOCKED_TILE_BIT) return@run

                        val actualPlane = if ((type.collision[1][x][z] and BRIDGE_TILE_BIT) == BRIDGE_TILE_BIT) plane - 1 else plane

                        if (actualPlane < 0) return@run

                        val baseX = type.regionX shl 6
                        val baseZ = type.regionZ shl 6
                        val location = Location(baseX + x, baseZ + z, actualPlane)
                        // TODO build zones and set collision there using this location
                        ZoneFlags.add(x and 0x7, z and 0x7, plane, FLOOR)
                    }
                }
            }
        }
    }

    private tailrec fun ByteReadPacket.decodeCollision(
        map: MapSquareEntryType,
        plane: Int,
        x: Int,
        z: Int
    ) {
        when (val opcode = readUByte().toInt()) {
            0 -> return
            1 -> { // Tile height
                discard(1)
                return
            }
            in 0..49 -> discard(1)
            in 0..81 -> map.collision[plane][x][z] = ((opcode - 49).toByte())
        }

        return decodeCollision(map, plane, x, z)
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
                var plane = (attributes shr 12) and 0x3

                val location = Location(localX, localZ, 1)

                if ((type.collision[plane][localX][localZ] and BLOCKED_TILE_BIT) == BLOCKED_TILE_BIT) {
                    plane--
                }

                if (plane < 0) return

                logger.debug { "ObjectId = $objectId Location = $location Shape = $shape Rotation = $rotation" }
            }
        }
    }

    companion object {
        const val BLOCKED_TILE_BIT = 0x1.toByte()
        const val BRIDGE_TILE_BIT = 0x2.toByte()

        const val VALID_X = 100
        const val VALID_Z = 256

        const val PLANES = 4
        const val SIZE = 64
    }
}
