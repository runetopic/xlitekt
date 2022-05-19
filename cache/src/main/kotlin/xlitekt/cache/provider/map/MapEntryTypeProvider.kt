package xlitekt.cache.provider.map

import com.github.michaelbull.logging.InlineLogger
import com.runetopic.cache.codec.decompress
import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readUByte
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import xlitekt.cache.provider.EntryTypeProvider
import xlitekt.shared.buffer.readIncrSmallSmart
import xlitekt.shared.buffer.readUShortSmart
import xlitekt.shared.inject
import xlitekt.shared.resource.MapSquares
import java.util.zip.ZipException

/**
 * @author Tyler Telis
 * @author Jordan Abraham
 */
class MapEntryTypeProvider : EntryTypeProvider<MapSquareEntryType>() {

    private val logger = InlineLogger()
    private val mapSquareResource by inject<MapSquares>()

    override fun load(): Map<Int, MapSquareEntryType> {
        val index = store.index(MAP_INDEX)
        // We only care about loading maps that we have configured in the xteas resource file.
        return mapSquareResource.values
            .parallelStream()
            .map { ByteReadPacket(index.group("m${it.mapsquare shr 8}_${it.mapsquare and 0xff}").data.decompress()).loadEntryType(MapSquareEntryType(it.mapsquare, it.mapsquare shr 8, it.mapsquare and 0xff)) }
            .toList()
            .associateBy(MapSquareEntryType::id)
    }

    override fun ByteReadPacket.loadEntryType(type: MapSquareEntryType): MapSquareEntryType {
        // Load the map file first.
        for (level in 0 until LEVELS) {
            for (x in 0 until MAP_SIZE) {
                for (z in 0 until MAP_SIZE) {
                    loadTerrain(type, level, x, z)
                }
            }
        }
        // Load the loc file next.
        val resource = mapSquareResource[type.id]!!
        check(resource.name == "l${type.regionX}_${type.regionZ}")
        val locs = store.index(MAP_INDEX).group("l${type.regionX}_${type.regionZ}")
        check(resource.nameHash == locs.nameHash)
        if (locs.data.isNotEmpty() && resource.key.isNotEmpty()) {
            try {
                ByteReadPacket(locs.data.decompress(resource.key.toIntArray())).loadLocs(type)
            } catch (exception: ZipException) {
                logger.warn { "Could not decompress and load locs. Perhaps the xtea keys are incorrect. GroupId=${locs.id}, MapSquare=${type.id}." }
            }
        }
        assertEmptyAndRelease()
        return type
    }

    private tailrec fun ByteReadPacket.loadTerrain(type: MapSquareEntryType, level: Int, x: Int, z: Int) {
        when (val opcode = readUByte().toInt()) {
            0 -> return
            1 -> { discard(1); return }
            in 2..49 -> discard(1)
            in 50..81 -> type.collision[level][x][z] = ((opcode - 49).toByte())
        }
        return loadTerrain(type, level, x, z)
    }

    private fun ByteReadPacket.loadLocs(type: MapSquareEntryType) {
        loadLocIds(type, -1)
        assertEmptyAndRelease()
    }

    private tailrec fun ByteReadPacket.loadLocIds(type: MapSquareEntryType, objectId: Int) {
        val offset = readIncrSmallSmart()
        if (offset == 0) return
        loadLocCollision(type, objectId + offset, 0)
        return loadLocIds(type, objectId + offset)
    }

    private tailrec fun ByteReadPacket.loadLocCollision(type: MapSquareEntryType, objectId: Int, packedLocation: Int) {
        val opcode = readUShortSmart()
        if (opcode == 0) return
        val attributes = readUByte().toInt()
        val shape = attributes shr 2
        val rotation = attributes and 0x3

        val packed = packedLocation + opcode - 1
        val localX = packed shr 6 and 0x3f
        val localZ = packed and 0x3f
        val level = (packed shr 12).let {
            if (type.collision[1][localX][localZ].toInt() and BRIDGE_TILE_BIT.toInt() == 2) it - 1 else it
        }

        if (level >= 0) {
            type.locations[level][localX][localZ].add(
                MapSquareEntryType.MapSquareLocation(
                    id = objectId,
                    x = localX,
                    z = localZ,
                    level = level,
                    shape = shape,
                    rotation = rotation
                )
            )
        }
        return loadLocCollision(type, objectId, packed)
    }

    companion object {
        const val BLOCKED_TILE_BIT = 0x1.toByte()
        const val BRIDGE_TILE_BIT = 0x2.toByte()

        const val LEVELS = 4
        const val MAP_SIZE = 64
    }
}
