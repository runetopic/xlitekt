package xlitekt.game.actor

import io.ktor.utils.io.core.ByteReadPacket
import xlitekt.game.actor.movement.Movement
import xlitekt.game.actor.movement.MovementSpeed
import xlitekt.game.actor.movement.MovementStep
import xlitekt.game.actor.movement.isValid
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.player.sendRebuildNormal
import xlitekt.game.actor.player.shouldRebuildMap
import xlitekt.game.actor.render.ActorRenderer
import xlitekt.game.actor.render.HitBarType
import xlitekt.game.actor.render.HitDamage
import xlitekt.game.actor.render.HitType
import xlitekt.game.actor.render.Render
import xlitekt.game.world.map.location.Location

abstract class Actor(
    open var location: Location
) {
    private val renderer = ActorRenderer()
    private val movement = Movement()

    var previousLocation: Location? = null
    var index = 0

    private var facingActorIndex = -1

    abstract fun totalHitpoints(): Int
    abstract fun currentHitpoints(): Int

    /**
     * Routes the actor movement waypoints to the input list.
     */
    fun route(locations: List<Location>) {
        actionReset()
        movement.route(locations)
    }

    /**
     * Routes the actor movement to a single waypoint with optional teleport speed.
     */
    fun route(location: Location, teleport: Boolean = false) {
        actionReset()
        movement.route(location, teleport)
    }

    /**
     * Toggles the actor movement speed between walking and running.
     * If the actor is a Player then this will also flag for movement and temporary movement type updates.
     */
    fun toggleMovementSpeed() {
        movement.movementSpeed = if (movement.movementSpeed.isRunning()) MovementSpeed.WALKING else MovementSpeed.RUNNING
        if (this is Player) {
            movementType(movement.movementSpeed::isRunning)
            temporaryMovementType(movement.movementSpeed::id)
        }
    }

    /**
     * Processes any pending movement this actor may have. This happens every tick.
     */
    fun processMovement(): MovementStep = movement.process(this, location).also {
        if (this is Player) {
            if (facingActorIndex != -1 && !it.isValid()) {
                faceActor { -1 }
            }
            if (it.isValid() && shouldRebuildMap()) sendRebuildNormal(false)
        }
    }

    fun hasPendingUpdate() = renderer.hasPendingUpdate()
    fun pendingUpdates() = renderer.pendingUpdates.values.toList()
    fun cacheUpdateBlock(render: Render, block: ByteReadPacket) {
        renderer.cachedUpdates.entries.removeIf { it.key::class == render::class }
        renderer.cachedUpdates[render] = block
    }
    fun cachedUpdates() = renderer.cachedUpdates
    fun clearPendingUpdates() = renderer.clearUpdates()

    /**
     * Use this when the player does an action. This will need work.
     */
    private fun actionReset() {
        movement.reset()
        if (facingActorIndex != -1 && this is Player) {
            faceActor { -1 }
            facingActorIndex = -1
        }
    }

    fun render(render: Render) {
        renderer.pendingUpdates[render::class] = render
        when (render) {
            is Render.FaceActor -> facingActorIndex = render.index
            else -> {}
        }
    }
}

inline fun Actor.faceActor(index: () -> Int) {
    render(Render.FaceActor(index.invoke()))
}

inline fun Actor.faceAngle(angle: () -> Int) {
    render(Render.FaceAngle(angle.invoke()))
}

inline fun Actor.animate(sequenceId: () -> Int) {
    render(Render.Sequence(sequenceId.invoke()))
}

inline fun Actor.spotAnimate(spotAnimationId: () -> Int) {
    render(Render.SpotAnimation(spotAnimationId.invoke()))
}

inline fun Actor.movementType(running: () -> Boolean) {
    render(Render.MovementType(running.invoke()))
}

inline fun Actor.temporaryMovementType(id: () -> Int) {
    render(Render.TemporaryMovementType(id.invoke()))
}

fun Actor.hit(hitBarType: HitBarType, source: Actor?, type: HitType, damage: Int, delay: Int) {
    render(
        Render.Hit(
            actor = this,
            hits = listOf(HitDamage(source, type, damage, delay)),
            bars = listOf(hitBarType)
        )
    )
}

inline fun Actor.chat(rights: Int, effects: Int, message: () -> String) {
    render(Render.PublicChat(message.invoke(), effects, rights))
}
