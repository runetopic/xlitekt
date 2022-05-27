package xlitekt.game.tick

import xlitekt.game.actor.Actor
import xlitekt.game.actor.movement.MovementStep
import xlitekt.game.actor.npc.NPC
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.render.block.invokeAlternativeDefinitionRenderingBlock
import xlitekt.game.actor.render.block.invokeHighDefinitionRenderingBlock
import xlitekt.game.actor.render.block.invokeLowDefinitionRenderingBlock
import xlitekt.game.packet.NPCInfoPacket
import xlitekt.game.packet.PlayerInfoPacket
import xlitekt.game.tick.NPCUpdates.MovementStepsNPCUpdates
import xlitekt.game.tick.PlayerUpdates.AlternativeHighDefinitionPlayerUpdates
import xlitekt.game.tick.PlayerUpdates.AlternativeLowDefinitionPlayerUpdates
import xlitekt.game.tick.PlayerUpdates.HighDefinitionPlayerUpdates
import xlitekt.game.tick.PlayerUpdates.LowDefinitionPlayerUpdates
import xlitekt.game.tick.PlayerUpdates.MovementStepsPlayerUpdates
import xlitekt.game.world.World
import xlitekt.shared.inject
import java.util.Optional
import java.util.concurrent.ConcurrentHashMap

/**
 * @author Jordan Abraham
 */
abstract class Synchronizer : Runnable {

    protected val world by inject<World>()
    private val playerMovementStepsUpdates = MovementStepsPlayerUpdates()
    private val npcMovementStepsUpdates = MovementStepsNPCUpdates()
    private val highDefinitionUpdates = HighDefinitionPlayerUpdates()
    private val lowDefinitionUpdates = LowDefinitionPlayerUpdates()
    private val alternativeHighDefinitionUpdates = AlternativeHighDefinitionPlayerUpdates()
    private val alternativeLowDefinitionUpdates = AlternativeLowDefinitionPlayerUpdates()

    protected fun Player.syncMovement(players: Map<Int, Player>) {
        processMovement(players)?.let { playerMovementStepsUpdates.add(this, it) }
    }

    protected fun NPC.syncMovement(players: Map<Int, Player>) {
        processMovement(players)?.let { npcMovementStepsUpdates.add(this, it) }
    }

    protected fun Player.syncRenderingBlocks() {
        highDefinitionUpdates.add(this, highDefinitionRenderingBlocks().invokeHighDefinitionRenderingBlock(this))
        lowDefinitionUpdates.add(this, lowDefinitionRenderingBlocks().invokeLowDefinitionRenderingBlock())
        alternativeHighDefinitionUpdates.add(this, alternativeHighDefinitionRenderingBlocks().invokeAlternativeDefinitionRenderingBlock())
        alternativeLowDefinitionUpdates.add(this, alternativeLowDefinitionRenderingBlocks().invokeAlternativeDefinitionRenderingBlock())
    }

    protected fun Player.syncClient(players: Map<Int, Player>) {
        write(PlayerInfoPacket(players, viewport, highDefinitionUpdates, lowDefinitionUpdates, alternativeHighDefinitionUpdates, alternativeLowDefinitionUpdates, playerMovementStepsUpdates))
        write(NPCInfoPacket(viewport, npcMovementStepsUpdates))
        invokeAndClearWritePool()
        resetDefinitionRenderingBlocks()
    }

    protected fun resetSynchronizer() {
        playerMovementStepsUpdates.clear()
        npcMovementStepsUpdates.clear()
        highDefinitionUpdates.clear()
        lowDefinitionUpdates.clear()
        alternativeHighDefinitionUpdates.clear()
        alternativeLowDefinitionUpdates.clear()
    }
}

sealed class PlayerUpdates<A : Actor, T : Any>(
    protected val updates: ConcurrentHashMap<Int, Optional<T>> = ConcurrentHashMap(World.MAX_PLAYERS)
) {
    class HighDefinitionPlayerUpdates : PlayerUpdates<Player, ByteArray>() {
        override fun add(actor: Player, any: ByteArray) {
            updates[actor.index] = Optional.of(any)
        }
    }

    class LowDefinitionPlayerUpdates : PlayerUpdates<Player, ByteArray>() {
        override fun add(actor: Player, any: ByteArray) {
            updates[actor.index] = Optional.of(any)
        }
    }

    class AlternativeHighDefinitionPlayerUpdates : PlayerUpdates<Player, ByteArray>() {
        override fun add(actor: Player, any: ByteArray) {
            updates[actor.index] = Optional.of(any)
        }
    }

    class AlternativeLowDefinitionPlayerUpdates : PlayerUpdates<Player, ByteArray>() {
        override fun add(actor: Player, any: ByteArray) {
            updates[actor.index] = Optional.of(any)
        }
    }

    class MovementStepsPlayerUpdates : PlayerUpdates<Player, MovementStep>() {
        override fun add(actor: Player, any: MovementStep) {
            updates[actor.index] = Optional.of(any)
        }
    }

    abstract fun add(actor: A, any: T)
    operator fun get(index: Int?) = if (index == null) null else updates[index]
    fun clear() = updates.clear()
}

sealed class NPCUpdates<A : Actor, T : Any?>(
    protected val updates: ConcurrentHashMap<Int, Optional<T>> = ConcurrentHashMap()
) {
    class MovementStepsNPCUpdates : NPCUpdates<NPC, MovementStep>() {
        override fun add(actor: NPC, any: MovementStep) {
            updates[actor.index] = Optional.of(any)
        }
    }

    abstract fun add(actor: A, any: T)
    operator fun get(index: Int?) = if (index == null) null else updates[index]
    fun clear() = updates.clear()
}
