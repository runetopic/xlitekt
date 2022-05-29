package xlitekt.game.actor

import xlitekt.game.actor.movement.Movement
import xlitekt.game.actor.movement.MovementSpeed
import xlitekt.game.actor.movement.MovementStep
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.player.rebuildNormal
import xlitekt.game.actor.render.HitBar
import xlitekt.game.actor.render.HitSplat
import xlitekt.game.actor.render.HitType
import xlitekt.game.actor.render.Render
import xlitekt.game.actor.render.Render.Appearance
import xlitekt.game.actor.render.Render.FaceActor
import xlitekt.game.actor.render.Render.FaceAngle
import xlitekt.game.actor.render.Render.Hit
import xlitekt.game.actor.render.Render.MovementType
import xlitekt.game.actor.render.Render.Sequence
import xlitekt.game.actor.render.Render.SpotAnimation
import xlitekt.game.actor.render.Render.TemporaryMovementType
import xlitekt.game.actor.render.block.AlternativeDefinitionRenderingBlock
import xlitekt.game.actor.render.block.HighDefinitionRenderingBlock
import xlitekt.game.actor.render.block.LowDefinitionRenderingBlock
import xlitekt.game.actor.render.block.PlayerRenderingBlockListener
import xlitekt.game.actor.render.block.RenderingBlock
import xlitekt.game.world.World
import xlitekt.game.world.map.location.Location
import xlitekt.game.world.map.zone.Zone
import xlitekt.shared.inject
import java.util.Optional
import java.util.TreeMap

abstract class Actor(
    open var location: Location
) {
    val movement = Movement()

    var previousLocation: Location? = null
    var index = 0

    internal var facingActorIndex = Optional.empty<Int>()
    private var activeZone = Optional.empty<Zone>()
    private val zones = mutableListOf<Zone>()

    /**
     * High definition rendering blocks used for local updates.
     * The key represents the index this rendering block should be placed to. This ordering is the same as the client implementation.
     */
    private val highDefinitionRenderingBlocks = TreeMap<Int, HighDefinitionRenderingBlock>()

    /**
     * Low definition rendering blocks used for non-local updates.
     * The key represents the index this rendering block should be placed to. This ordering is the same as the client implementation.
     */
    private val lowDefinitionRenderingBlocks = TreeMap<Int, LowDefinitionRenderingBlock>()

    /**
     * Alternative rendering blocks used for both local and non-local updates.
     * This is only used for blocks that requires the outside player perspective.
     * Example of this is for hit splat tinting as the hit block requires the outside player to check for the varbit.
     *
     * The key represents the index this rendering block should be placed to. This ordering is the same as the client implementation.
     */
    private val alternativeHighDefinitionRenderingBlocks = TreeMap<Int, AlternativeDefinitionRenderingBlock>()
    private val alternativeLowDefinitionRenderingBlocks = TreeMap<Int, AlternativeDefinitionRenderingBlock>()

    abstract fun totalHitpoints(): Int
    abstract fun currentHitpoints(): Int

    /**
     * Processes any pending movement this actor may have. This happens every tick.
     */
    internal fun processMovement(players: Map<Int, Player>): MovementStep? = movement.process(this, location).also {
        location = it?.location ?: location
        if (this is Player) {
            if (it == null) {
                // When the player is not processing movement steps.
                if (facingActorIndex.isPresent) {
                    faceActor { -1 }
                }
            } else {
                // When the player is processing movement steps.
                if (shouldRebuildMap()) rebuildNormal(players) { false }
            }
        }
        if (shouldRebuildZones() && activeZone.isPresent) {
            if (activeZone.isPresent) {
                activeZone.get().leaveZone(this)
            }
            world.zone(location)?.enterZone(this)
        }
    }

    /**
     * Returns if this actor has any high definition rendering blocks.
     */
    fun hasHighDefinitionRenderingBlocks() = highDefinitionRenderingBlocks.isNotEmpty()

    /**
     * Returns a list of this actors high definition rendering blocks.
     */
    internal fun highDefinitionRenderingBlocks() = highDefinitionRenderingBlocks.values

    /**
     * Adds a high definition rendering block to a low definition one.
     */
    internal fun setLowDefinitionRenderingBlock(highDefinitionRenderingBlock: HighDefinitionRenderingBlock, bytes: ByteArray) {
        val lowDefinitionRenderingBlock = LowDefinitionRenderingBlock(
            render = highDefinitionRenderingBlock.render,
            renderingBlock = highDefinitionRenderingBlock.renderingBlock,
            bytes = if (highDefinitionRenderingBlock.render.hasAlternative()) bytes.copyOfRange(0, bytes.size / 2) else bytes
        )
        // Insert the rendering block into the TreeMap based on its index. This is to preserve order based on the client.
        lowDefinitionRenderingBlocks[highDefinitionRenderingBlock.renderingBlock.index] = lowDefinitionRenderingBlock
    }

    /**
     * Returns a list of this actors low definition rendering blocks.
     */
    internal fun lowDefinitionRenderingBlocks() = lowDefinitionRenderingBlocks.values

    /**
     * Happens after this actor has finished processing by the game loop.
     */
    internal fun resetDefinitionRenderingBlocks() {
        // Clear the high definition blocks.
        highDefinitionRenderingBlocks.clear()
        // We only want to persist these types of low definition blocks.
        lowDefinitionRenderingBlocks.values.removeIf {
            it.render !is Appearance && it.render !is FaceAngle && it.render !is MovementType
        }
        alternativeHighDefinitionRenderingBlocks.clear()
        // We only want to persist these types of low definition blocks.
        alternativeLowDefinitionRenderingBlocks.values.removeIf {
            it.render !is Appearance && it.render !is FaceAngle && it.render !is MovementType
        }
    }

    /**
     * Adds an alternative rendering block to this player.
     */
    internal fun setAlternativeRenderingBlock(render: Render, renderingBlock: RenderingBlock, bytes: ByteArray) {
        val alternativeDefinitionRenderingBlock = AlternativeDefinitionRenderingBlock(
            render = render,
            renderingBlock = renderingBlock,
            bytes = if (render.hasAlternative()) bytes.copyOfRange(bytes.size / 2, bytes.size) else bytes
        )
        alternativeHighDefinitionRenderingBlocks[renderingBlock.index] = alternativeDefinitionRenderingBlock
        alternativeLowDefinitionRenderingBlocks[renderingBlock.index] = alternativeDefinitionRenderingBlock
    }

    /**
     * Returns a map of this players alternative rendering blocks.
     */
    internal fun alternativeHighDefinitionRenderingBlocks() = alternativeHighDefinitionRenderingBlocks.values
    internal fun alternativeLowDefinitionRenderingBlocks() = alternativeLowDefinitionRenderingBlocks.values

    private fun shouldRebuildZones(): Boolean {
        if (activeZone.isPresent) {
            return location.zoneId != activeZone.get().location.zoneId
        }
        return true
    }

    /**
     * Flags this actor with a new pending rendering block.
     */
    fun render(render: Render) {
        val renderingBlock = PlayerRenderingBlockListener.listeners[render::class] ?: return
        // Insert the rendering block into the TreeMap based on its index. This is to preserve order based on the client.
        highDefinitionRenderingBlocks[renderingBlock.index] = HighDefinitionRenderingBlock(render, renderingBlock)
        when (render) {
            is FaceActor -> facingActorIndex = Optional.of(render.index)
            else -> {} // TODO
        }
    }

    fun zone() = if (activeZone.isPresent) activeZone.get() else {
        world.zone(location)?.apply {
            // Should never happen but this is a safe measure.
            activeZone = Optional.of(this)
        }
    }
    fun setZone(zone: Zone) {
        activeZone = Optional.of(zone)
    }

    fun zones() = zones.toList()
    fun setZones(removed: List<Zone>, added: List<Zone>) {
        zones.removeAll(removed)
        zones.addAll(added)
    }

    private companion object {
        val world by inject<World>()
    }
}

/**
 * Use this when the player does an action. This will need work.
 */
fun Actor.actionReset() {
    movement.reset()
    if (facingActorIndex.isPresent && this is Player) {
        faceActor { -1 }
        facingActorIndex = Optional.empty()
    }
}

/**
 * Routes the actor movement waypoints to the input list.
 */
inline fun Actor.route(locations: () -> List<Location>) {
    actionReset()
    movement.route(locations.invoke())
}

/**
 * Routes the actor movement to a specified location.
 */
inline fun Actor.routeTo(location: () -> Location) {
    actionReset()
    movement.route(location.invoke(), false)
}

/**
 * Routes the actor movement to a single location with teleport speed.
 */
inline fun Actor.routeTeleport(location: () -> Location) {
    actionReset()
    movement.route(location.invoke(), true)
}

/**
 * Toggles the actor movement speed between walking and running.
 * If the actor is a Player then this will also flag for movement and temporary movement type updates.
 */
inline fun Actor.speed(running: () -> Boolean) = running.invoke().also {
    movement.movementSpeed = if (it) MovementSpeed.RUNNING else MovementSpeed.WALKING
    if (this is Player) {
        movementType { it }
        temporaryMovementType(movement.movementSpeed::id)
    }
}

inline fun Actor.faceActor(index: () -> Int) {
    render(FaceActor(index.invoke()))
}

inline fun Actor.faceAngle(angle: () -> Int) {
    render(FaceAngle(angle.invoke()))
}

inline fun Actor.animate(sequenceId: () -> Int) {
    render(Sequence(sequenceId.invoke()))
}

inline fun Actor.spotAnimate(spotAnimationId: () -> Int) {
    render(SpotAnimation(spotAnimationId.invoke()))
}

inline fun Actor.movementType(running: () -> Boolean) {
    render(MovementType(running.invoke()))
}

inline fun Actor.temporaryMovementType(id: () -> Int) {
    render(TemporaryMovementType(id.invoke()))
}

inline fun Actor.hit(hitBar: HitBar, source: Actor?, type: HitType, delay: Int, damage: () -> Int) {
    render(
        Hit(
            actor = this,
            hits = listOf(HitSplat(source, type, damage.invoke(), delay)),
            bars = listOf(hitBar)
        )
    )
}

inline fun Actor.chat(rights: Int, effects: Int, message: () -> String) {
    render(Render.PublicChat(message.invoke(), effects, rights))
}
