package xlitekt.game.actor

import io.ktor.utils.io.core.ByteReadPacket
import xlitekt.game.actor.movement.Movement
import xlitekt.game.actor.movement.MovementStep
import xlitekt.game.actor.movement.isValid
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.player.rebuildZones
import xlitekt.game.actor.player.sendRebuildNormal
import xlitekt.game.actor.player.shouldRebuildMap
import xlitekt.game.actor.player.shouldRefreshZones
import xlitekt.game.actor.render.ActorRenderer
import xlitekt.game.actor.render.HitBarType
import xlitekt.game.actor.render.HitDamage
import xlitekt.game.actor.render.HitType
import xlitekt.game.actor.render.Render
import xlitekt.game.world.map.location.Location

abstract class Actor(
    open var location: Location
) {
    protected val renderer by lazy { ActorRenderer(this) }
    val movement by lazy { Movement(this) }

    var previousLocation: Location? = null
    var index = 0

    var mapLoaded = false

    abstract fun totalHitpoints(): Int
    abstract fun currentHitpoints(): Int

    fun processMovement(): MovementStep = movement.process(location).also {
        if (this is Player) {
            if (it.isValid() && shouldRebuildMap()) sendRebuildNormal(false)
            // else if (it.isValid() && shouldRefreshZones() && !shouldRebuildMap()) rebuildZones(true)
        }
    }

    fun hit(hitBarType: HitBarType, source: Actor?, type: HitType, damage: Int, delay: Int) {
        renderer.hit(
            Render.Hit(
                actor = this,
                hits = listOf(HitDamage(source, type, damage, delay)),
                bars = listOf(hitBarType)
            )
        )
    }

    fun publicChat(message: String, packedEffects: Int, rights: Int) = renderer.publicChat(message, packedEffects, rights)
    fun customOptions(prefix: String, infix: String, suffix: String) = renderer.customOptions(prefix, infix, suffix)
    fun animate(id: Int, delay: Int = 0) = renderer.sequence(id, delay)
    fun faceActor(index: Int) = renderer.faceActor(index)
    fun faceAngle(direction: Int) = renderer.faceAngle(direction)
    // fun forceMove(forceMovement: Render.ForceMovement) = renderer.forceMove(forceMovement)
    fun overheadChat(text: String) = renderer.overheadChat(text)
    // fun recolor(recolor: Render.Recolor) = renderer.recolor(recolor)
    fun spotAnimate(id: Int, speed: Int = 0, height: Int = 0, rotation: Int = 0) = renderer.spotAnimation(id, speed, height, rotation)
    fun transmog(id: Int) = renderer.transmog(id)
    fun temporaryMovementType(id: Int) = renderer.temporaryMovementType(id)
    fun movementType(running: Boolean) = renderer.movementType(running)

    fun hasPendingUpdate() = renderer.hasPendingUpdate()
    fun pendingUpdates() = renderer.pendingUpdates.values.toList()
    fun cacheUpdateBlock(render: Render, block: ByteReadPacket) {
        renderer.cachedUpdates.entries.removeIf { it.key::class == render::class }
        renderer.cachedUpdates[render] = block
    }
    fun cachedUpdates() = renderer.cachedUpdates

    fun reset() {
        renderer.clearUpdates()
    }
}
