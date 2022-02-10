package com.runetopic.xlitekt.cache.provider.config.varbit

import com.runetopic.xlitekt.cache.provider.EntryType

data class VarBitEntryType(
    override var id: Int,
    var index: Int = -1,
    var leastSignificantBit: Int = -1,
    var mostSignificantBit: Int = -1
) : EntryType(id)
