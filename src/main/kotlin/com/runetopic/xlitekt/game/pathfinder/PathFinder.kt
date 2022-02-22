package com.runetopic.xlitekt.game.pathfinder

import org.rsmod.pathfinder.Route
import org.rsmod.pathfinder.SmartPathFinder

object PathFinder {
    fun smartRoute(srcX: Int, srcY: Int, destX: Int, destY: Int, level: Int): Route {
        val pf = SmartPathFinder()
        return pf.findPath(flags, srcX, srcY, destX, destY)
    }
}
