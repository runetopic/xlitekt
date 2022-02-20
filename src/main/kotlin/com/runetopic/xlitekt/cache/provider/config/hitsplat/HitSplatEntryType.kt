package com.runetopic.xlitekt.cache.provider.config.hitsplat

import com.runetopic.xlitekt.cache.provider.EntryType

/**
 * @author Jordan Abraham
 */
data class HitSplatEntryType(
    override val id: Int,
    var fontId: Int = -1,
    var textColor: Int = 16777215,
    var field1934: Int = 70,
    var spriteId1: Int = -1,
    var spriteId3: Int = -1,
    var spriteId2: Int = -1,
    var spriteId4: Int = -1,
    var field1929: Int = 0,
    var field1940: Int = 0,
    var field1943: Int = -1,
    var field1942: String = "",
    var field1946: Int = -1,
    var field1944: Int = 0,
    var transformVarbit: Int = -1,
    var transformVarp: Int = -1,
    var transforms: List<Int> = listOf()
) : EntryType(id)
