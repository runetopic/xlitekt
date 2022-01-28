package com.runetopic.xlitekt.game.actor

import com.runetopic.xlitekt.game.actor.render.ActorRenderer
import com.runetopic.xlitekt.game.tile.Tile

abstract class Actor(
    open var tile: Tile
) {
    var previousTile: Tile? = null
    var index = 0
    val renderer = ActorRenderer()
}
