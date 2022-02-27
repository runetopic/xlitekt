package xlitekt.cache.provider.config.hitsplat

import xlitekt.cache.provider.EntryType

/**
 * @author Jordan Abraham
 */
data class HitSplatEntryType(
    override val id: Int,
    var fontId: Int = -1,
    var textColor: Int = 16777215,
    var displayCycles: Int = 70,
    var leftSpriteId: Int = -1,
    var backgroundSprite: Int = -1,
    var spriteId2: Int = -1,
    var rightSpriteId: Int = -1,
    var scrollToOffsetX: Int = 0,
    var scrollToOffsetY: Int = 0,
    var fadeStartCycle: Int = -1,
    var stringFormat: String = "",
    var useDamage: Int = -1,
    var textOffsetY: Int = 0,
    var transformVarbit: Int = -1,
    var transformVarp: Int = -1,
    var transforms: List<Int> = listOf()
) : EntryType(id)
