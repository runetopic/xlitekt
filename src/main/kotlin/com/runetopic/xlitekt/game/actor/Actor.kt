package com.runetopic.xlitekt.game.actor

import com.runetopic.xlitekt.game.actor.render.ActorRenderer
import com.runetopic.xlitekt.game.actor.render.Render
import com.runetopic.xlitekt.game.tile.Tile

abstract class Actor(
    open var tile: Tile
) {
    var previousTile: Tile? = null
    var index = 0
    protected val renderer = ActorRenderer()

    // TODO maybe move the combat stuff out somewhere else
    val nextHits = mutableListOf<Render.HitDamage>()
    val nextHitBars = mutableListOf<HitBarType>()

    abstract fun totalHitpoints(): Int
    abstract fun currentHitpoints(): Int

    fun hit(hitBarType: HitBarType, source: Actor?, type: HitType, damage: Int, delay: Int) {
        nextHits += renderer.hit(source, type, damage, delay)
        nextHitBars += hitBarType
    }

    fun overheadChat(text: String) = renderer.overheadChat(text)
    fun faceTile(tile: Tile) = renderer.faceTile(tile)
    fun pendingUpdates() = renderer.pendingUpdates
    fun hasPendingUpdate() = renderer.hasPendingUpdate()

    fun clear() {
        renderer.clearUpdates()
    }
}
