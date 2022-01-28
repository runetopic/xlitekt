package com.runetopic.xlitekt.game.actor.npc

import com.runetopic.xlitekt.game.actor.Actor
import com.runetopic.xlitekt.game.tile.Tile

class NPC(
    val id: Int,
    override var tile: Tile
) : Actor(tile)
