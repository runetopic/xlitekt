package com.runetopic.xlitekt.cache.provider.map

import com.runetopic.xlitekt.cache.provider.EntryType
import com.runetopic.xlitekt.cache.provider.map.MapEntryTypeProvider.Companion.LEVELS
import com.runetopic.xlitekt.cache.provider.map.MapEntryTypeProvider.Companion.MAP_SIZE

/**
 * @author Tyler Telis
 */
data class MapSquareEntryType(
    override val id: Int,
    val regionX: Int,
    val regionZ: Int,
    var collision: Array<Array<ByteArray>> = Array(LEVELS) { Array(MAP_SIZE) { ByteArray(MAP_SIZE) } }
) : EntryType(id) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MapSquareEntryType

        if (id != other.id) return false
        if (regionX != other.regionX) return false
        if (regionZ != other.regionZ) return false
        if (!collision.contentDeepEquals(other.collision)) return false

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
