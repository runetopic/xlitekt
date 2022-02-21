package com.runetopic.xlitekt.game.actor.npc

import com.runetopic.xlitekt.game.actor.Actor
import com.runetopic.xlitekt.game.location.Location

class NPC(
    val id: Int,
    override var location: Location
) : Actor(location) {
    override fun totalHitpoints(): Int = 100
    override fun currentHitpoints(): Int = 100

    fun faceTile(location: Location) = renderer.faceTile(location)
    fun setCustomCombatLevel(level: Int) = renderer.setCustomCombatLevel(level)
}
