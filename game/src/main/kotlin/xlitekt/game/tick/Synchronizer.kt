package xlitekt.game.tick

import xlitekt.game.actor.movement.MovementStep
import xlitekt.game.actor.npc.NPC
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.render.block.invokeAlternativeDefinitionRenderingBlock
import xlitekt.game.actor.render.block.invokeHighDefinitionRenderingBlock
import xlitekt.game.actor.render.block.invokeLowDefinitionRenderingBlock
import xlitekt.game.packet.NPCInfoPacket
import xlitekt.game.packet.PlayerInfoPacket
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
        HighDefinitionPlayerUpdates.add(index, highDefinitionRenderingBlocks().invokeHighDefinitionRenderingBlock(this))
        LowDefinitionPlayerUpdates.add(index, lowDefinitionRenderingBlocks().invokeLowDefinitionRenderingBlock())
        AlternativeHighDefinitionPlayerUpdates.add(index, alternativeHighDefinitionRenderingBlocks().invokeAlternativeDefinitionRenderingBlock())
        AlternativeLowDefinitionPlayerUpdates.add(index, alternativeLowDefinitionRenderingBlocks().invokeAlternativeDefinitionRenderingBlock())
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
                movementStepsUpdates = MovementStepsNPCUpdates
            )
        )
        invokeAndClearWritePool()
        resetDefinitionRenderingBlocks()
    }

    protected fun resetSynchronizer() {
        MovementStepsPlayerUpdates.clear()
        MovementStepsNPCUpdates.clear()
        HighDefinitionPlayerUpdates.clear()
        LowDefinitionPlayerUpdates.clear()
        AlternativeHighDefinitionPlayerUpdates.clear()
        AlternativeLowDefinitionPlayerUpdates.clear()
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

    object MovementStepsNPCUpdates : NPCInfoUpdates<MovementStep>()

    fun add(index: Int, update: T) {
        updates[index] = Optional.of(update)
    }
    fun clear() = updates.clear()
}
