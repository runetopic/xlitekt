package com.runetopic.xlitekt.game.actor.npc

import com.runetopic.xlitekt.game.actor.Actor
import com.runetopic.xlitekt.game.tile.Tile

class NPC(
    val id: Int,
    override var tile: Tile
) : Actor(tile) {
    override fun totalHitpoints(): Int = 100
    override fun currentHitpoints(): Int = 100

    fun faceTile(tile: Tile) = renderer.faceTile(tile)
    fun setCustomCombatLevel(level: Int) = renderer.setCustomCombatLevel(level)
}
