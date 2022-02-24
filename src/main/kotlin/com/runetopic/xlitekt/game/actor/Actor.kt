package com.runetopic.xlitekt.game.actor

import com.runetopic.xlitekt.game.actor.movement.Movement
import com.runetopic.xlitekt.game.actor.movement.MovementSpeed
import com.runetopic.xlitekt.game.actor.render.ActorRenderer
import com.runetopic.xlitekt.game.actor.render.HitBarType
import com.runetopic.xlitekt.game.actor.render.HitType
import com.runetopic.xlitekt.game.actor.render.Render
import com.runetopic.xlitekt.game.world.map.location.Location

abstract class Actor(
    open var location: Location
) {
    protected val renderer = ActorRenderer()

    var previousLocation: Location? = null
    var index = 0
    var nextTick = false

    var movementSpeed = MovementSpeed.WALK

    // TODO maybe move the combat stuff out somewhere else
    val nextHits = mutableListOf<Render.HitDamage>()
    val nextHitBars = mutableListOf<HitBarType>()

    val movement get() = Movement(this)

    abstract fun totalHitpoints(): Int
    abstract fun currentHitpoints(): Int

    fun hit(hitBarType: HitBarType, source: Actor?, type: HitType, damage: Int, delay: Int) {
        nextHits += renderer.hit(source, type, damage, delay)
        nextHitBars += hitBarType
    }

    fun publicChat(message: String, packedEffects: Int) = renderer.publicChat(message, packedEffects)
    fun customOptions(prefix: String, infix: String, suffix: String) = renderer.customOptions(prefix, infix, suffix)
    fun animate(id: Int, delay: Int = 0) = renderer.sequence(id, delay)
    fun faceActor(index: Int) = renderer.faceActor(index)
    fun faceDirection(direction: Int) = renderer.faceDirection(direction)
    // fun forceMove(forceMovement: Render.ForceMovement) = renderer.forceMove(forceMovement)
    fun overheadChat(text: String) = renderer.overheadChat(text)
    // fun recolor(recolor: Render.Recolor) = renderer.recolor(recolor)
    fun spotAnimate(id: Int, speed: Int = 0, height: Int = 0, rotation: Int = 0) = renderer.spotAnimation(id, speed, height, rotation)
    fun transmog(id: Int) = renderer.transmog(id)
    fun setTemporaryMovementType(id: Int) = renderer.temporaryMovementType(id)

    fun hasPendingUpdate() = renderer.hasPendingUpdate()
    fun pendingUpdates() = renderer.pendingUpdates
    fun reset() {
        renderer.clearUpdates()
    }
}
