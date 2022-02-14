package com.runetopic.xlitekt.cache.provider.config.loc

import com.runetopic.xlitekt.cache.provider.EntryType

/**
 * @author Jordan Abraham
 */
data class LocEntryType(
    override val id: Int,
    var name: String = "null",
    var sizeX: Int = 1,
    var sizeY: Int = 1,
    var interactType: Int = 2,
    var boolean1: Boolean = true,
    var int1: Int = -1,
    var clipType: Int = -1,
    var nonFlatShading: Boolean = false,
    var modelClipped: Boolean = false,
    var animationId: Int = -1,
    var int2: Int = 16,
    var ambient: Int = 0,
    var contrast: Int = 0,
    var actions: List<String> = List(5) { "null" },
    var mapIconId: Int = -1,
    var mapSceneId: Int = -1,
    var isRotated: Boolean = false,
    var clipped: Boolean = true,
    var modelSizeX: Int = 128,
    var modelHeight: Int = 128,
    var modelSizeY: Int = 128,
    var offsetX: Int = 0,
    var offsetHeight: Int = 0,
    var offsetY: Int = 0,
    var boolean2: Boolean = false,
    var isSolid: Boolean = false,
    var int3: Int = -1,
    var transformVarbit: Int = -1,
    var transformVarp: Int = -1,
    var ambientSoundId: Int = -1,
    var int7: Int = 0,
    var int5: Int = 0,
    var int6: Int = 0,
    var boolean3: Boolean = true,
    var transforms: List<Int> = listOf(),
    var params: Map<Int, Any> = mapOf()
) : EntryType(id)
