package com.runetopic.xlitekt.game.actor

import com.runetopic.xlitekt.game.actor.render.ActorRenderer
import com.runetopic.xlitekt.game.tile.Tile

abstract class Actor(
    var tile: Tile
) {
    var previousTile: Tile? = null
    var pid = 0
    val renderer = ActorRenderer()
}
