package com.runetopic.xlitekt.game.actor

import com.runetopic.xlitekt.game.actor.render.ActorRenderer
import com.runetopic.xlitekt.game.actor.render.HitBarType
import com.runetopic.xlitekt.game.actor.render.HitType
import com.runetopic.xlitekt.game.actor.render.Render
import com.runetopic.xlitekt.game.tile.Tile

abstract class Actor(
    open var tile: Tile
) {
    protected val renderer = ActorRenderer()
    var previousTile: Tile? = null
    var index = 0

    // TODO maybe move the combat stuff out somewhere else
    val nextHits = mutableListOf<Render.HitDamage>()
    val nextHitBars = mutableListOf<HitBarType>()

    abstract fun totalHitpoints(): Int
    abstract fun currentHitpoints(): Int

    fun hit(hitBarType: HitBarType, source: Actor?, type: HitType, damage: Int, delay: Int) {
        nextHits += renderer.hit(source, type, damage, delay)
        nextHitBars += hitBarType
    }

    fun publicChat(message: String, packedEffects: Int) = renderer.publicChat(message, packedEffects)
    fun customOptions(string1: String, string2: String, string3: String) = renderer.customOptions(string1, string2, string3)
    fun animate(animation: Render.Animation) = renderer.animate(animation)
    fun faceActor(index: Int) = renderer.faceActor(index)
    fun faceTile(tile: Tile) = renderer.faceTile(tile)
    fun forceMove(forceMovement: Render.ForceMovement) = renderer.forceMove(forceMovement)
    fun hasPendingUpdate() = renderer.hasPendingUpdate()
    fun overheadChat(text: String) = renderer.overheadChat(text)
    fun pendingUpdates() = renderer.pendingUpdates
    fun recolor(recolor: Render.Recolor) = renderer.recolor(recolor)
    fun spotAnimation(spotAnimation: Render.SpotAnimation) = renderer.spotAnimation(spotAnimation)
    fun transmog(id: Int) = renderer.transmog(id)
    fun setTemporaryMovementType(id: Int) = renderer.temporaryMovementType(id)

    fun reset() {
        renderer.clearUpdates()
    }
}
