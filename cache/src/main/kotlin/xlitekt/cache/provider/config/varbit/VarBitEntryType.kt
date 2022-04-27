package xlitekt.cache.provider.config.varbit

import kotlinx.serialization.Serializable
import xlitekt.cache.provider.EntryType

@Serializable
data class VarBitEntryType(
    override val id: Int,
    var index: Int = -1,
    var leastSignificantBit: Int = -1,
    var mostSignificantBit: Int = -1
) : EntryType(id)
