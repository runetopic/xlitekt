package xlitekt.game.actor

import xlitekt.game.actor.movement.Movement
import xlitekt.game.actor.render.ActorRenderer
import xlitekt.game.actor.render.HitBarType
import xlitekt.game.actor.render.HitType
import xlitekt.game.actor.render.Render
import xlitekt.game.world.map.location.Location

abstract class Actor(
    open var location: Location
) {
    protected val renderer = ActorRenderer()

    var teleported = false
    var nextLocation: Location? = null
    var previousLocation: Location? = null
    var index = 0

    // TODO maybe move the combat stuff out somewhere else
    val nextHits = mutableListOf<Render.HitDamage>()
    val nextHitBars = mutableListOf<HitBarType>()

    val movement by lazy { Movement(this) }

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
    fun temporaryMovementType(id: Int) = renderer.temporaryMovementType(id)
    fun movementType(running: Boolean) = renderer.movementType(running)

    fun hasPendingUpdate() = renderer.hasPendingUpdate()
    fun pendingUpdates() = renderer.pendingUpdates.values.toList()

    fun reset() {
        renderer.clearUpdates()
    }
}
