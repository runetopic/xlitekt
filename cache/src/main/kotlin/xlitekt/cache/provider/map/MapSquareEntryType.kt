package xlitekt.cache.provider.map

import xlitekt.cache.provider.EntryType
import xlitekt.cache.provider.map.MapEntryTypeProvider.Companion.LEVELS
import xlitekt.cache.provider.map.MapEntryTypeProvider.Companion.MAP_SIZE

/**
 * @author Tyler Telis
 */
data class MapSquareEntryType(
    override val id: Int,
    val regionX: Int,
    val regionZ: Int,
    var collision: Array<Array<ByteArray>> = Array(LEVELS) { Array(MAP_SIZE) { ByteArray(MAP_SIZE) } },
    val locations: Array<Array<Array<MutableList<MapSquareLocation>>>> = Array(LEVELS) {
        Array(MAP_SIZE) {
            Array(MAP_SIZE) {
                mutableListOf()
            }
        }
    }
) : EntryType(id) {

    data class MapSquareLocation(
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
        if (!collision.contentDeepEquals(other.collision)) return false
        if (!locations.contentDeepEquals(other.locations)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + regionX
        result = 31 * result + regionZ
        result = 31 * result + collision.contentDeepHashCode()
        return result
    }
}
