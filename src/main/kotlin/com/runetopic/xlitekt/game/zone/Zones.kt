package com.runetopic.xlitekt.game.zone

import com.runetopic.xlitekt.plugin.koin.inject
import com.runetopic.xlitekt.shared.resource.MapSquares

private const val ZONES = 2048 * 2048 * 4

object Zones {
    val collisionMap = arrayOfNulls<IntArray?>(ZONES)
}
