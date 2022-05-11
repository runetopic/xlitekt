package xlitekt.cache.provider.config.npc

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import xlitekt.cache.provider.EntryType

/**
 * @author Jordan Abraham
 */
@Serializable
data class NPCEntryType(
    override val id: Int,
    var name: String = "null",
    var size: Int = 1,
    var idleSequence: Int = -1,
    var turnLeftSequence: Int = -1,
    var turnRightSequence: Int = -1,
    var walkSequence: Int = -1,
    var walkBackSequence: Int = -1,
    var walkLeftSequence: Int = -1,
    var walkRightSequence: Int = -1,
    var runSequence: Int = -1,
    var runBackSequence: Int = -1,
    var runLeftSequence: Int = -1,
    var runRightSequence: Int = -1,
    var crawlSequence: Int = -1,
    var crawlBackSequence: Int = -1,
    var crawlLeftSequence: Int = -1,
    var crawlRightSequence: Int = -1,
    var actions: List<String> = List(5) { "null" },
    var drawMapDot: Boolean = true,
    var combatLevel: Int = -1,
    var widthScale: Int = 128,
    var heightScale: Int = 128,
    var isVisible: Boolean = false,
    var ambient: Int = 0,
    var contrast: Int = 0,
    var headIconPrayer: Int = -1,
    var rotation: Int = 32,
    var transformVarbit: Int = -1,
    var transformVarp: Int = -1,
    var isInteractable: Boolean = true,
    var isClickable: Boolean = true,
    var isFollower: Boolean = false,
    var models: List<Int> = listOf(),
    var transforms: List<Int> = listOf(),
    var params: Map<Int, @Contextual Any> = mapOf()
) : EntryType(id)
