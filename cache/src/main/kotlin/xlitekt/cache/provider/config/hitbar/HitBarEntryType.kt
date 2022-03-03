package xlitekt.cache.provider.config.hitbar

import xlitekt.cache.provider.EntryType

/**
 * @author Jordan Abraham
 */
data class HitBarEntryType(
    override val id: Int,
    var int1: Int = 255,
    var int2: Int = 255,
    var int3: Int = -1,
    var field1798: Int = 1,
    var int5: Int = 70,
    var frontSpriteId: Int = -1,
    var backgroundSpriteId: Int = -1,
    var scale: Int = 30,
    var padding: Int = 0,
) : EntryType(id)
