package xlitekt.cache.provider.map

import xlitekt.cache.provider.EntryType
import xlitekt.cache.provider.map.MapSquareEntryTypeProvider.Companion.LEVELS
import xlitekt.cache.provider.map.MapSquareEntryTypeProvider.Companion.MAP_SIZE

/**
 * @author Tyler Telis
 */
data class MapSquareEntryType(
    override val id: Int,
    val regionX: Int = id shr 8,
    val regionZ: Int = id and 0xff,
    var terrain: Array<Array<Array<MapSquareTerrainLocation?>>> = Array(LEVELS) { Array(MAP_SIZE) { arrayOfNulls(MAP_SIZE) } },
    val locs: Array<Array<Array<MutableList<MapSquareLocLocation>>>> = Array(LEVELS) {
        Array(MAP_SIZE) {
            Array(MAP_SIZE) {
                mutableListOf()
            }
        }
    }
) : EntryType(id) {

    data class MapSquareTerrainLocation(
        val height: Int,
        val overlayId: Int,
        val overlayPath: Int,
        val overlayRotation: Int,
        val collision: Int,
        val underlayId: Int
    )

    data class MapSquareLocLocation(
        val id: Int,
        val x: Int,
        val z: Int,
        val level: Int,
        val shape: Int,
        val rotation: Int
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MapSquareEntryType

        if (id != other.id) return false
        if (regionX != other.regionX) return false
        if (regionZ != other.regionZ) return false
        if (!terrain.contentDeepEquals(other.terrain)) return false
        if (!locs.contentDeepEquals(other.locs)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + regionX
        result = 31 * result + regionZ
        result = 31 * result + terrain.contentDeepHashCode()
        result = 31 * result + locs.contentDeepHashCode()
        return result
    }
}
