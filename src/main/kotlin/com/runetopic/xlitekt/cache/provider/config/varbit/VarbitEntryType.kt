package com.runetopic.xlitekt.cache.provider.config.varbit

import com.runetopic.xlitekt.cache.provider.EntryType

data class VarbitEntryType(
    private val id: Int,
    private val index: Int,
    private val leastSignificantBit: Int,
    private val mostSignificantBit: Int
) : EntryType
