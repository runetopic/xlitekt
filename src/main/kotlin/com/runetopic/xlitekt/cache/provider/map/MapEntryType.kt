package com.runetopic.xlitekt.cache.provider.map

import com.runetopic.xlitekt.cache.provider.EntryType

data class MapEntryType(
    override val id: Int,
    val regionX: Int,
    val regionZ: Int,
    var collison: Array<Array<IntArray>> = Array(PLANES) { Array(SIZE) { IntArray(SIZE) } }
) : EntryType(id) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MapEntryType

        if (id != other.id) return false
        if (regionX != other.regionX) return false
        if (regionZ != other.regionZ) return false
        if (!collison.contentDeepEquals(other.collison)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + regionX
        result = 31 * result + regionZ
        result = 31 * result + collison.contentDeepHashCode()
        return result
    }

    companion object {
        const val PLANES = 4
        const val SIZE = 64
    }
}
