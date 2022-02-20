package com.runetopic.xlitekt.cache.provider.map

import com.github.michaelbull.logging.InlineLogger
import com.runetopic.xlitekt.cache.provider.EntryTypeProvider
import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readUByte
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import kotlin.system.measureTimeMillis

class MapEntryTypeProvider : EntryTypeProvider<MapEntryType>() {
    private val logger = InlineLogger()

    override fun load(): Map<Int, MapEntryType> {
        val mapSquares = mutableMapOf<Int, MapEntryType>()

        val latch = CountDownLatch(VALID_X * VALID_X)
        val threads = Runtime.getRuntime().availableProcessors()
        val pool = Executors.newFixedThreadPool(threads - 2)

        var count = 0

        val time = measureTimeMillis {
            val index = store.index(5)

            repeat(VALID_X) { x ->
                repeat(VALID_Z) { z ->
                    val regionId = (x shl 8) or z

                    pool.execute {
                        val data = index.group("m${x}_$z").data

                        if (data.isEmpty()) {
                            latch.countDown()
                            return@execute
                        }

                        mapSquares[regionId] = ByteReadPacket(data).loadEntryType(MapEntryType(regionId, x, z))
                        count++
                        latch.countDown()
                    }
                }
            }
        }
        latch.await()
        pool.shutdown()
        logger.debug { "Finished loading maps. Count = $count. Took $time ms." }
        return mapSquares
    }

    override fun ByteReadPacket.loadEntryType(type: MapEntryType): MapEntryType {
        repeat(4) { plane ->
            repeat(64) { x ->
                repeat(64) { z ->
                    decodeCollisionFlags(type, plane, x, z)
                }
            }
        }

        return type
    }

    private tailrec fun ByteReadPacket.decodeCollisionFlags(
        type: MapEntryType,
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
            in 0..81 -> type.collison[plane][x][z] = (opcode - 49)
        }

        return decodeCollisionFlags(type, plane, x, z)
    }

    companion object {
        const val VALID_X = 256
        const val VALID_Z = 100
    }
}
