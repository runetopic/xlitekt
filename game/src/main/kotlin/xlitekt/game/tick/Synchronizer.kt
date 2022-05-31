package xlitekt.game.tick

import xlitekt.game.actor.movement.MovementStep
import xlitekt.game.actor.npc.NPC
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.render.block.invokeAlternativeDefinitionPlayerRenderingBlocks
import xlitekt.game.actor.render.block.invokeHighDefinitionNPCRenderingBlocks
import xlitekt.game.actor.render.block.invokeHighDefinitionPlayerRenderingBlocks
import xlitekt.game.actor.render.block.invokeLowDefinitionPlayerRenderingBlocks
import xlitekt.game.packet.NPCInfoPacket
import xlitekt.game.packet.PlayerInfoPacket
import xlitekt.game.tick.NPCInfoUpdates.HighDefinitionNPCUpdates
import xlitekt.game.tick.NPCInfoUpdates.MovementStepsNPCUpdates
import xlitekt.game.tick.PlayerInfoUpdates.AlternativeHighDefinitionPlayerUpdates
import xlitekt.game.tick.PlayerInfoUpdates.AlternativeLowDefinitionPlayerUpdates
import xlitekt.game.tick.PlayerInfoUpdates.HighDefinitionPlayerUpdates
import xlitekt.game.tick.PlayerInfoUpdates.LowDefinitionPlayerUpdates
import xlitekt.game.tick.PlayerInfoUpdates.MovementStepsPlayerUpdates
import xlitekt.game.world.World
import xlitekt.shared.inject
import java.util.Optional
import java.util.concurrent.ConcurrentHashMap

/**
 * @author Jordan Abraham
 */
abstract class Synchronizer : Runnable {

    protected val world by inject<World>()

    protected fun Player.syncMovement(players: Map<Int, Player>) {
        processMovement(players)?.let { MovementStepsPlayerUpdates.add(index, it) }
    }

    protected fun NPC.syncMovement(players: Map<Int, Player>) {
        processMovement(players)?.let { MovementStepsNPCUpdates.add(index, it) }
    }

    protected fun Player.syncRenderingBlocks() {
        val highDefinition = highDefinitionRenderingBlocks()
        if (highDefinition.isNotEmpty()) HighDefinitionPlayerUpdates.add(index, highDefinition.invokeHighDefinitionPlayerRenderingBlocks(this))
        LowDefinitionPlayerUpdates.add(index, lowDefinitionRenderingBlocks().invokeLowDefinitionPlayerRenderingBlocks())
        if (highDefinition.isNotEmpty()) AlternativeHighDefinitionPlayerUpdates.add(index, alternativeHighDefinitionRenderingBlocks().invokeAlternativeDefinitionPlayerRenderingBlocks())
        AlternativeLowDefinitionPlayerUpdates.add(index, alternativeLowDefinitionRenderingBlocks().invokeAlternativeDefinitionPlayerRenderingBlocks())
    }

    protected fun NPC.syncRenderingBlocks() {
        val blocks = highDefinitionRenderingBlocks()
        if (blocks.isEmpty()) return
        HighDefinitionNPCUpdates.add(index, blocks.invokeHighDefinitionNPCRenderingBlocks())
        resetDefinitionRenderingBlocks()
    }

    protected fun Player.syncClient(players: Map<Int, Player>) {
        write(
            PlayerInfoPacket(
                players = players,
                viewport = viewport,
                highDefinitionUpdates = HighDefinitionPlayerUpdates,
                lowDefinitionUpdates = LowDefinitionPlayerUpdates,
                alternativeHighDefinitionUpdates = AlternativeHighDefinitionPlayerUpdates,
                alternativeLowDefinitionUpdates = AlternativeLowDefinitionPlayerUpdates,
                movementStepsUpdates = MovementStepsPlayerUpdates
            )
        )
        write(
            NPCInfoPacket(
                viewport = viewport,
                highDefinitionUpdates = HighDefinitionNPCUpdates,
                movementStepsUpdates = MovementStepsNPCUpdates
            )
        )
        invokeAndClearWritePool()
        resetDefinitionRenderingBlocks()
    }

    protected fun resetSynchronizer() {
        // Player
        MovementStepsPlayerUpdates.clear()
        HighDefinitionPlayerUpdates.clear()
        LowDefinitionPlayerUpdates.clear()
        AlternativeHighDefinitionPlayerUpdates.clear()
        AlternativeLowDefinitionPlayerUpdates.clear()
        // NPC
        HighDefinitionNPCUpdates.clear()
        MovementStepsNPCUpdates.clear()
    }
}

internal sealed class PlayerInfoUpdates<T : Any>(
    private val updates: ConcurrentHashMap<Int, Optional<T>> = ConcurrentHashMap(World.MAX_PLAYERS)
) : Map<Int, Optional<T>> by updates {

    object HighDefinitionPlayerUpdates : PlayerInfoUpdates<ByteArray>()
    object LowDefinitionPlayerUpdates : PlayerInfoUpdates<ByteArray>()
    object AlternativeHighDefinitionPlayerUpdates : PlayerInfoUpdates<ByteArray>()
    object AlternativeLowDefinitionPlayerUpdates : PlayerInfoUpdates<ByteArray>()
    object MovementStepsPlayerUpdates : PlayerInfoUpdates<MovementStep>()

    fun add(index: Int, update: T) {
        updates[index] = Optional.of(update)
    }
    fun clear() = updates.clear()
}

internal sealed class NPCInfoUpdates<T : Any>(
    private val updates: ConcurrentHashMap<Int, Optional<T>> = ConcurrentHashMap()
) : Map<Int, Optional<T>> by updates {

    object HighDefinitionNPCUpdates : NPCInfoUpdates<ByteArray>()
    object MovementStepsNPCUpdates : NPCInfoUpdates<MovementStep>()

    fun add(index: Int, update: T) {
        updates[index] = Optional.of(update)
    }
    fun clear() = updates.clear()
}
