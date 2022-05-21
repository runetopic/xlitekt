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
                    type.terrain[level][x][z] = loadTerrain(type, level, x, z)
                }
            }
        }
        // Load the loc file next.
        val groupName = "l${type.regionX}_${type.regionZ}"
        val resource = mapSquareResource[type.id]!!
        check(resource.name == groupName)
        val locs = store.index(MAP_INDEX).group(groupName)
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

    private tailrec fun ByteReadPacket.loadTerrain(
        type: MapSquareEntryType,
        level: Int,
        x: Int,
        z: Int,
        height: Int = 0,
        overlayId: Int = 0,
        overlayPath: Int = 0,
        overlayRotation: Int = 0,
        collision: Int = 0,
        underlayId: Int = 0
    ): MapSquareEntryType.MapSquareTerrainLocation {
        return when (val opcode = readUByte().toInt()) {
            0 -> MapSquareEntryType.MapSquareTerrainLocation(height, overlayId, overlayPath, overlayRotation, collision, underlayId)
            1 -> MapSquareEntryType.MapSquareTerrainLocation(readUByte().toInt(), overlayId, overlayPath, overlayRotation, collision, underlayId)
            else -> loadTerrain(
                type = type,
                level = level,
                x = x,
                z = z,
                height = height,
                overlayId = if (opcode in 2..49) readUByte().toInt() else overlayId,
                overlayPath = if (opcode in 2..49) (opcode - 2) / 4 else overlayPath,
                overlayRotation = if (opcode in 2..49) opcode - 2 and 3 else overlayRotation,
                collision = if (opcode in 50..81) opcode - 49 else collision,
                underlayId = if (opcode > 81) opcode - 81 else underlayId
            )
        }
    }

    private fun ByteReadPacket.loadLocs(type: MapSquareEntryType) {
        loadLocIds(type, -1)
        assertEmptyAndRelease()
    }

    private tailrec fun ByteReadPacket.loadLocIds(type: MapSquareEntryType, locId: Int) {
        val offset = readIncrSmallSmart()
        if (offset == 0) return
        loadLocCollision(type, locId + offset, 0)
        return loadLocIds(type, locId + offset)
    }

    private tailrec fun ByteReadPacket.loadLocCollision(type: MapSquareEntryType, objectId: Int, packedLocation: Int) {
        val offset = readUShortSmart()
        if (offset == 0) return
        val attributes = readUByte().toInt()
        val shape = attributes shr 2
        val rotation = attributes and 0x3

        val packed = packedLocation + offset - 1
        val localX = packed shr 6 and 0x3f
        val localZ = packed and 0x3f
        val level = (packed shr 12).let {
            if (type.terrain[1][localX][localZ]!!.collision and BRIDGE_TILE_BIT == 2) it - 1 else it
        }

        if (level >= 0) {
            type.locs[level][localX][localZ].add(
                MapSquareEntryType.MapSquareLocLocation(
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
        const val BLOCKED_TILE_BIT = 0x1
        const val BRIDGE_TILE_BIT = 0x2

        const val LEVELS = 4
        const val MAP_SIZE = 64
    }
}
