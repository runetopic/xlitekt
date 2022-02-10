package com.runetopic.xlitekt.cache.provider.config.varbit

import com.runetopic.xlitekt.cache.provider.EntryType

data class VarBitEntryType(
    override val id: Int,
    val index: Int,
    val leastSignificantBit: Int,
    val mostSignificantBit: Int
) : EntryType(id)
