package xlitekt.game.actor

import it.unimi.dsi.fastutil.ints.IntArrayList
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.delay
import org.jctools.maps.NonBlockingHashMapLong
import xlitekt.game.actor.bonus.Bonuses
import xlitekt.game.actor.movement.Movement
import xlitekt.game.actor.movement.MovementRequest
import xlitekt.game.actor.movement.MovementSpeed
import xlitekt.game.actor.movement.MovementStep
import xlitekt.game.actor.movement.pf.PathFinders
import xlitekt.game.actor.npc.NPC
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.player.drainRunEnergy
import xlitekt.game.actor.player.message
import xlitekt.game.actor.player.rebuildNormal
import xlitekt.game.actor.player.resetMiniMapFlag
import xlitekt.game.actor.render.HitBar
import xlitekt.game.actor.render.HitSplat
import xlitekt.game.actor.render.HitType
import xlitekt.game.actor.render.Render
import xlitekt.game.actor.render.Render.Appearance
import xlitekt.game.actor.render.Render.FaceActor
import xlitekt.game.actor.render.Render.FaceAngle
import xlitekt.game.actor.render.Render.FaceLocation
import xlitekt.game.actor.render.Render.Hit
import xlitekt.game.actor.render.Render.MovementType
import xlitekt.game.actor.render.Render.OverheadChat
import xlitekt.game.actor.render.Render.PublicChat
import xlitekt.game.actor.render.Render.Sequence
import xlitekt.game.actor.render.Render.SpotAnimation
import xlitekt.game.actor.render.Render.TemporaryMovementType
import xlitekt.game.actor.render.block.AlternativeDefinitionRenderingBlock
import xlitekt.game.actor.render.block.HighDefinitionRenderingBlock
import xlitekt.game.actor.render.block.LowDefinitionRenderingBlock
import xlitekt.game.actor.render.block.NPCRenderingBlockListener
import xlitekt.game.actor.render.block.PlayerRenderingBlockListener
import xlitekt.game.actor.render.block.RenderingBlock
import xlitekt.game.queue.ActorQueue
import xlitekt.game.queue.QueuePriority
import xlitekt.game.queue.shouldProcess
import xlitekt.game.world.World
import xlitekt.game.world.map.GameObject
import xlitekt.game.world.map.Location
import xlitekt.game.world.map.directionTo
import xlitekt.game.world.map.zone.Zone
import xlitekt.shared.inject
import java.util.Optional
import java.util.concurrent.Executors
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * @author Tyler Telis
 * @author Jordan Abraham
 */
abstract class Actor(
    open var location: Location
) {
    val movement = Movement()
    val bonuses = Bonuses()
    var previousLocation = Location.None

    abstract val queue: ActorQueue<out Actor>

    /**
     * This actor index.
     */
    var index = 0
    inline val indexL get() = index.toLong()

    /**
     * Represents this actor current location zone.
     */
    private var zone = Optional.empty<Zone>()

    /**
     * Represents this actor 7x7 build area of zones.
     */
    private val zones = HashSet<Zone>(49)

    /**
     * High definition rendering blocks used for local updates.
     * The key represents the index this rendering block should be placed to. This ordering is the same as the client implementation.
     */
    private val highDefinitionRenderingBlocks = NonBlockingHashMapLong<HighDefinitionRenderingBlock>()

    /**
     * Low definition rendering blocks used for non-local updates.
     * The key represents the index this rendering block should be placed to. This ordering is the same as the client implementation.
     */
    private val lowDefinitionRenderingBlocks = NonBlockingHashMapLong<LowDefinitionRenderingBlock>()

    /**
     * Alternative rendering blocks used for both local and non-local updates.
     * This is only used for blocks that requires the outside player perspective.
     * Example of this is for hit splat tinting as the hit block requires the outside player to check for the varbit.
     *
     * The key represents the index this rendering block should be placed to. This ordering is the same as the client implementation.
     */
    private val alternativeHighDefinitionRenderingBlocks = NonBlockingHashMapLong<AlternativeDefinitionRenderingBlock>()
    private val alternativeLowDefinitionRenderingBlocks = NonBlockingHashMapLong<AlternativeDefinitionRenderingBlock>()

    /**
     * Represents the actor index that this actor is currently facing.
     * This is currently only used by players.
     */
    internal var facingActorIndex = Optional.empty<Int>()

    abstract fun totalHitpoints(): Int
    abstract fun currentHitpoints(): Int

    /**
     * Processes any pending movement this actor may have. This happens every tick.
     */
    internal fun processMovement(players: NonBlockingHashMapLong<Player>): MovementStep? = movement.process(this).also { step ->
        location = step?.location ?: location

        if (step == null) {
            movement.movementRequest.ifPresent {
                val reached = location.packedLocation == it.waypoints.last() && !it.alternative
                if (this is Player && !reached) {
                    message { "You can't reach that." }
                    resetMiniMapFlag()
                }
                if (reached) {
                    it.reachAction?.invoke()
                }
                movement.movementRequest = Optional.empty()
            }
            resetMovement()
        } else {
            if (this is Player) {
                if (shouldRebuildMap()) rebuildNormal(players) { false }

                drainRunEnergy()
            }
        }
        if (shouldRebuildZones() && zone.isPresent) {
            if (zone.isPresent) {
                zone.get().leaveZone(this, world.zone(location))
            }
        }
    }

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
        lowDefinitionRenderingBlocks[highDefinitionRenderingBlock.renderingBlock.indexL] = lowDefinitionRenderingBlock
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
        alternativeHighDefinitionRenderingBlocks[renderingBlock.indexL] = alternativeDefinitionRenderingBlock
        alternativeLowDefinitionRenderingBlocks[renderingBlock.indexL] = alternativeDefinitionRenderingBlock
    }

    /**
     * Returns a map of this players alternative rendering blocks.
     */
    internal fun alternativeHighDefinitionRenderingBlocks() = alternativeHighDefinitionRenderingBlocks.values
    internal fun alternativeLowDefinitionRenderingBlocks() = alternativeLowDefinitionRenderingBlocks.values

    /**
     * Returns if this actor needs a zones rebuild.
     */
    private fun shouldRebuildZones() = zone().location.zoneId != location.zoneId

    /**
     * Flags this actor with a new pending rendering block.
     * @param render The render to use.
     */
    fun render(render: Render) {
        if (this is NPC) {
            val renderingBlock = NPCRenderingBlockListener.listeners[render::class] ?: return
            // Insert the rendering block into the TreeMap based on its index. This is to preserve order based on the client.
            highDefinitionRenderingBlocks[renderingBlock.indexL] = HighDefinitionRenderingBlock(render, renderingBlock)
        } else if (this is Player) {
            val renderingBlock = PlayerRenderingBlockListener.listeners[render::class] ?: return
            // Insert the rendering block into the TreeMap based on its index. This is to preserve order based on the client.
            highDefinitionRenderingBlocks[renderingBlock.indexL] = HighDefinitionRenderingBlock(render, renderingBlock)
            when (render) {
                is FaceActor -> facingActorIndex = Optional.of(render.index)
                else -> {} // TODO
            }
        }
    }

    /**
     * Returns the current zone this actor is inside of.
     */
    fun zone(): Zone = zone.orElse(world.zone(location))

    /**
     * Set this actor current zone.
     * @param zone The zone to set.
     */
    fun setZone(zone: Zone) {
        this.zone = Optional.of(zone)
    }

    /**
     * Returns a list of this actor zones.
     */
    fun zones() = zones

    /**
     * Set this actor zones with a list of zones being removed and a list of zones being added.
     * @param removed The zones being removed from this actor.
     * @param added The zones being added to this actor.
     */
    fun setZones(removed: Set<Zone>, added: Set<Zone>) {
        zones.removeAll(removed)
        zones.addAll(added)
    }

    private companion object {
        val world by inject<World>()
    }
}

/**
 * Angle this actor to a location.
 * If this actor is a Player this will flag the faceAngle render.
 * If this actor is a NPC this will flag the faceLocation render.
 * @param location The location to angle to.
 */
fun Actor.angleTo(location: Location) {
    if (this is Player) {
        faceAngle(this.location.directionTo(location)::angle)
    } else if (this is NPC) {
        faceLocation { location }
    }
}

/**
 * Angle this actor to a game object.
 * If this actor is a Player this will flag the faceAngle render.
 * If this actor is a NPC this will flag the faceLocation render.
 * @param gameObject The game object to angle to.
 */
fun Actor.angleTo(gameObject: GameObject) {
    if (this is Player) {
        faceAngle(location.directionTo(Location(gameObject.angleX, gameObject.angleZ))::angle)
    } else if (this is NPC) {
        faceLocation(gameObject::location)
    }
}

/**
 * Angle this actor to a npc.
 * If this actor is a Player this will flag the faceAngle render.
 * If this actor is a NPC this will flag the faceLocation render.
 * @param npc The npc to angle to.
 */
fun Actor.angleTo(npc: NPC) {
    if (this is Player) {
        faceAngle(location.directionTo(npc.location)::angle)
    } else if (this is NPC) {
        faceLocation(npc::location)
    }
}

/**
 * Cancels weak actions this actor is doing.
 *
 */
fun Actor.cancelWeak() {
    queue.cancelWeak()
}

/**
 * Resets movement related information.
 * This also cancels the facingActorIndex flag.
 */
fun Actor.resetMovement(resetFlag: Boolean = false) {
    movement.reset()
    if (resetFlag && this is Player) {
        resetMiniMapFlag()
    }
    facingActorIndex.ifPresent {
        if (this is Player) {
            faceActor { -1 }
        }
        facingActorIndex = Optional.empty()
    }
}

/**
 * Route the actor movement to a single location with teleport speed.
 * @param location The location to teleport to.
 */
inline fun Actor.teleportTo(location: () -> Location) {
    cancelWeak()
    movement.route(location.invoke(), true)
}

/**
 * Toggle the actor movement speed between walking and running.
 * If the actor is a Player then this will also flag for movement and temporary movement type updates.
 */
inline fun Actor.speed(running: () -> Boolean) = running.invoke().also {
    movement.movementSpeed = if (it) MovementSpeed.Running else MovementSpeed.Walking
    if (this is Player) {
        movementType { it }
        temporaryMovementType(movement.movementSpeed::id)
    }
}

/**
 * Route this actor to a location.
 * @param location The location to route to.
 * @param reachAction A callback function to invoke when the actor reaches the destination.
 */
fun Actor.routeTo(location: Location, reachAction: (() -> Unit)? = null) {
    queue.strong {
        resetMovement()
        val route = PathFinders.findPath(
            smart = this is Player,
            srcX = this.location.x,
            srcZ = this.location.z,
            destX = location.x,
            destZ = location.z,
            level = this.location.level
        )
        movement.route(
            MovementRequest(
                reachAction,
                IntArrayList(route.coords.size).also { points ->
                    route.coords.map { points.add(Location(it.x, it.y, this.location.level).packedLocation) }
                },
                route.failed,
                route.alternative
            )
        )
    }
}

/**
 * Route this actor to a game object.
 * @param gameObject The game object to route to.
 * @param reachAction A callback function to invoke when the actor reaches the destination.
 */
fun Actor.routeTo(gameObject: GameObject, reachAction: (() -> Unit)? = null) {
    queue.strong {
        resetMovement()
        val dest = gameObject.location
        val rotation = gameObject.rotation
        val shape = gameObject.shape
        val width = gameObject.entry?.width
        val height = gameObject.entry?.height
        val route = PathFinders.findPath(
            smart = this is Player,
            srcX = location.x,
            srcZ = location.z,
            destX = dest.x,
            destZ = dest.z,
            level = location.level,
            destWidth = if (rotation == 0 || rotation == 2) width else height,
            destHeight = if (rotation == 0 || rotation == 2) height else width,
            rotation = rotation,
            shape = shape
        )
        movement.route(
            MovementRequest(
                reachAction,
                IntArrayList(route.coords.size).also { points ->
                    route.coords.map { points.add(Location(it.x, it.y, this@routeTo.location.level).packedLocation) }
                },
                route.failed,
                route.alternative
            )
        )
    }
}

/**
 * Route this actor to a npc.
 * @param npc The npc to route to.
 * @param reachAction A callback function to invoke when the actor reaches the destination.
 */
fun Actor.routeTo(npc: NPC, reachAction: (() -> Unit)? = null) {
    queue.strong {
        faceActor(npc::index)
        resetMovement()
        val dest = npc.location
        val route = PathFinders.findPath(
            smart = this is Player,
            srcX = location.x,
            srcZ = location.z,
            destX = dest.x,
            destZ = dest.z,
            level = location.level,
            destWidth = npc.entry?.size,
            destHeight = npc.entry?.size,
            shape = 10
        )
        movement.route(
            MovementRequest(
                reachAction,
                IntArrayList(route.coords.size).also { points ->
                    route.coords.map { points.add(Location(it.x, it.y, this@routeTo.location.level).packedLocation) }
                },
                route.failed,
                route.alternative
            )
        )
    }
}

/**
 * Render this actor to face an actor.
 * @param index The actor index to invoke.
 */
inline fun Actor.faceActor(index: () -> Int) {
    render(FaceActor(index.invoke()))
}

/**
 * Render this actor to face an angle.
 * This is only supported by player actors.
 * @param angle The angle to invoke.
 */
private inline fun Actor.faceAngle(angle: () -> Int) {
    if (this is NPC) throw IllegalStateException("NPC does not support this render.")
    render(FaceAngle(angle.invoke()))
}

/**
 * Render this actor to animate a sequence.
 * @param sequenceId The sequence to animate this actor with.
 */
inline fun Actor.animate(sequenceId: () -> Int) {
    render(Sequence(sequenceId.invoke()))
}

/**
 * Render this actor to spot animate.
 * @param spotAnimationId The spot animation to animate this actor with.
 */
inline fun Actor.spotAnimate(spotAnimationId: () -> Int) {
    render(SpotAnimation(spotAnimationId.invoke()))
}

/**
 * Render this actor to update the movement type.
 * This is only supported by player actors.
 * @param running The running flag to invoke on.
 */
inline fun Actor.movementType(running: () -> Boolean) {
    if (this is NPC) throw IllegalStateException("NPC does not support this render.")
    render(MovementType(running.invoke()))
}

/**
 * Render this actor to temporary movement type.
 * This is only supported by player actors.
 * @param id The temporary movement type to invoke on.
 */
inline fun Actor.temporaryMovementType(id: () -> Int) {
    if (this is NPC) throw IllegalStateException("NPC does not support this render.")
    render(TemporaryMovementType(id.invoke()))
}

/**
 * Render this actor with hits.
 * @param hitBar The hitbar associated with this hit.
 * @param source The source actor performing this hit.
 * @param type The type of hit this is.
 * @param damage The amount of damage to invoke.
 */
inline fun Actor.hit(hitBar: HitBar, source: Actor?, type: HitType, delay: Int, damage: () -> Int) {
    render(
        Hit(
            actor = this,
            hits = listOf(HitSplat(source, type, damage.invoke(), delay)),
            bars = listOf(hitBar)
        )
    )
}

/**
 * Render this actor to public chat.
 * This is only supported by player actors.
 * @param rights The player actor rights.
 * @param effects The chat effects.
 * @param message The public chat message to invoke.
 */
inline fun Actor.chat(rights: Int, effects: Int, message: () -> String) {
    if (this is NPC) throw IllegalStateException("NPC does not support this render.")
    render(PublicChat(message.invoke(), effects, rights))
}

/**
 * Render this actor to overhead chat.
 * @param message The overhead chat message to invoke.
 */
inline fun Actor.overheadChat(message: () -> String) {
    render(OverheadChat(message.invoke()))
}

/**
 * Render this actor to face a location.
 * This is only supported by npc actors.
 * @param location The location for this npc actor to face.
 */
private inline fun Actor.faceLocation(location: () -> Location) {
    if (this is Player) throw IllegalStateException("Player does not support this render.")
    render(FaceLocation(location.invoke()))
}

/**
 * This will process all the actor's queued scripts.
 */
fun Actor.process() {
    if (this@process is Player && queue.any { it.priority == QueuePriority.Strong }) {
        interfaces.closeModal() // This is currently only closing 1 modal interface. I dunno is OSRS supports more than 1.
    }

    while (queue.isNotEmpty()) {
        val count = processQueue()

        if (count == 0) break
    }
}

/**
 * This goes through the queue and if the script is a strong or soft type, it will close the modal interfaces before executing.
 * This also checks if the script itself should process.
 */
fun Actor.processQueue(): Int {
    var count = 0

    queue.removeIf { script ->
        if (this is Player && (script.priority == QueuePriority.Strong || script.priority == QueuePriority.Soft)) interfaces.closeModal()

        if (script.shouldProcess()) {
            script.process()
            count++
            return@removeIf true
        }

        return@removeIf false
    }

    return count
}
