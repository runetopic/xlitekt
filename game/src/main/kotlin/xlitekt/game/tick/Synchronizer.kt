package xlitekt.game.tick

import xlitekt.game.actor.movement.MovementStep
import xlitekt.game.actor.npc.NPC
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.render.block.invokeAlternativeDefinitionRenderingBlock
import xlitekt.game.actor.render.block.invokeHighDefinitionRenderingBlock
import xlitekt.game.actor.render.block.invokeLowDefinitionRenderingBlock
import xlitekt.game.packet.NPCInfoPacket
import xlitekt.game.packet.PlayerInfoPacket
import xlitekt.game.world.World
import xlitekt.shared.inject
import java.util.Optional
import java.util.concurrent.ConcurrentHashMap

/**
 * @author Jordan Abraham
 */
abstract class Synchronizer : Runnable {

    protected val world by inject<World>()
    private val playerMovementStepsUpdates = PlayerMovementStepsUpdates()
    private val npcMovementStepsUpdates = NPCMovementStepsUpdates()
    private val highDefinitionUpdates = HighDefinitionUpdates()
    private val lowDefinitionUpdates = LowDefinitionUpdates()
    private val alternativeHighDefinitionUpdates = AlternativeHighDefinitionUpdates()
    private val alternativeLowDefinitionUpdates = AlternativeLowDefinitionUpdates()

    protected fun Player.syncMovement(players: Map<Int, Player>) {
        playerMovementStepsUpdates.add(this, processMovement(players))
    }

    protected fun NPC.syncMovement(players: Map<Int, Player>) {
        npcMovementStepsUpdates.add(this, processMovement(players))
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

class HighDefinitionUpdates(
    private val updates: ConcurrentHashMap<Int, Optional<ByteArray>> = ConcurrentHashMap(World.MAX_PLAYERS)
) : Map<Int, Optional<ByteArray>> by updates {
    internal fun add(player: Player, bytes: ByteArray?) {
        updates[player.index] = Optional.ofNullable(bytes)
    }

    internal fun clear() = updates.clear()
}

class LowDefinitionUpdates(
    private val updates: ConcurrentHashMap<Int, Optional<ByteArray>> = ConcurrentHashMap(World.MAX_PLAYERS)
) : Map<Int, Optional<ByteArray>> by updates {
    internal fun add(player: Player, bytes: ByteArray?) {
        updates[player.index] = Optional.ofNullable(bytes)
    }

    internal fun clear() = updates.clear()
}

class AlternativeHighDefinitionUpdates(
    private val updates: ConcurrentHashMap<Int, Optional<ByteArray>> = ConcurrentHashMap(World.MAX_PLAYERS)
) : Map<Int, Optional<ByteArray>> by updates {
    internal fun add(player: Player, bytes: ByteArray?) {
        updates[player.index] = Optional.ofNullable(bytes)
    }

    internal fun clear() = updates.clear()
}

class AlternativeLowDefinitionUpdates(
    private val updates: ConcurrentHashMap<Int, Optional<ByteArray>> = ConcurrentHashMap(World.MAX_PLAYERS)
) : Map<Int, Optional<ByteArray>> by updates {
    internal fun add(player: Player, bytes: ByteArray?) {
        updates[player.index] = Optional.ofNullable(bytes)
    }

    internal fun clear() = updates.clear()
}

class PlayerMovementStepsUpdates(
    private val updates: ConcurrentHashMap<Int, Optional<MovementStep>> = ConcurrentHashMap(World.MAX_PLAYERS)
) : Map<Int, Optional<MovementStep>> by updates {
    internal fun add(player: Player, movementStep: MovementStep?) {
        updates[player.index] = Optional.ofNullable(movementStep)
    }

    internal fun clear() = updates.clear()
}

class NPCMovementStepsUpdates(
    private val updates: ConcurrentHashMap<Int, Optional<MovementStep>> = ConcurrentHashMap()
) : Map<Int, Optional<MovementStep>> by updates {
    internal fun add(npc: NPC, movementStep: MovementStep?) {
        updates[npc.index] = Optional.ofNullable(movementStep)
    }

    internal fun clear() = updates.clear()
}
