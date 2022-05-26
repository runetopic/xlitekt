package xlitekt.game.tick

import xlitekt.game.actor.movement.MovementStep
import xlitekt.game.actor.npc.NPC
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.render.Render
import xlitekt.game.actor.render.block.HighDefinitionRenderingBlock
import xlitekt.game.actor.render.block.LowDefinitionRenderingBlock
import xlitekt.game.actor.render.block.createHighDefinitionsMask
import xlitekt.game.actor.render.block.createLowDefinitionsMask
import xlitekt.game.actor.render.block.invokeHighDefinitionRenderingBlock
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
    private val alternativeUpdates = AlternativeUpdates()

    protected fun Player.syncMovement(players: Map<Int, Player>) {
        playerMovementStepsUpdates.add(this, processMovement(players, location))
    }

    protected fun NPC.syncMovement(players: Map<Int, Player>) {
        npcMovementStepsUpdates.add(this, processMovement(players, location))
    }

    protected fun Player.syncRenderingBlocks() {
        for (block in highDefinitionRenderingBlocks()) {
            highDefinitionUpdates.add(this, block, block.invokeHighDefinitionRenderingBlock(this))
        }
        highDefinitionUpdates.addMask(this, highDefinitionRenderingBlocks().createHighDefinitionsMask())
        for (block in lowDefinitionRenderingBlocks()) {
            lowDefinitionUpdates.add(this, block, block.bytes)
        }
        lowDefinitionUpdates.addMask(this, lowDefinitionRenderingBlocks().createLowDefinitionsMask())
        for (block in alternativeRenderingBlocks()) {
            alternativeUpdates.add(this, block.key, block.value)
        }
    }

    protected fun Player.syncClient(players: Map<Int, Player>) {
        write(PlayerInfoPacket(players, viewport, highDefinitionUpdates, lowDefinitionUpdates, alternativeUpdates, playerMovementStepsUpdates))
        write(NPCInfoPacket(viewport, npcMovementStepsUpdates))
        invokeAndClearWritePool()
        resetDefinitionRenderingBlocks()
    }

    protected fun resetSynchronizer() {
        playerMovementStepsUpdates.clear()
        npcMovementStepsUpdates.clear()
        highDefinitionUpdates.clear()
        lowDefinitionUpdates.clear()
        alternativeUpdates.clear()
    }
}

class HighDefinitionUpdates(
    private val updates: ConcurrentHashMap<Int, MutableMap<HighDefinitionRenderingBlock, ByteArray>> = ConcurrentHashMap(World.MAX_PLAYERS)
) : Map<Int, MutableMap<HighDefinitionRenderingBlock, ByteArray>> by updates {
    val masks: ConcurrentHashMap<Int, ByteArray> = ConcurrentHashMap(World.MAX_PLAYERS)

    internal fun add(player: Player, block: HighDefinitionRenderingBlock, bytes: ByteArray) {
        if (updates[player.index] == null) {
            updates[player.index] = mutableMapOf()
        }
        updates[player.index]!![block] = bytes
    }

    internal fun addMask(player: Player, bytes: ByteArray) {
        masks[player.index] = bytes
    }

    internal fun clear() {
        updates.clear()
        masks.clear()
    }
}

class LowDefinitionUpdates(
    private val updates: ConcurrentHashMap<Int, MutableMap<LowDefinitionRenderingBlock, ByteArray>> = ConcurrentHashMap(World.MAX_PLAYERS)
) : Map<Int, MutableMap<LowDefinitionRenderingBlock, ByteArray>> by updates {
    val masks: ConcurrentHashMap<Int, ByteArray> = ConcurrentHashMap(World.MAX_PLAYERS)

    internal fun add(player: Player, block: LowDefinitionRenderingBlock, bytes: ByteArray) {
        if (updates[player.index] == null) {
            updates[player.index] = mutableMapOf()
        }
        updates[player.index]!![block] = bytes
    }

    internal fun addMask(player: Player, bytes: ByteArray) {
        masks[player.index] = bytes
    }

    internal fun clear() {
        updates.clear()
        masks.clear()
    }
}

class AlternativeUpdates(
    private val updates: ConcurrentHashMap<Int, MutableMap<Render, ByteArray>> = ConcurrentHashMap(World.MAX_PLAYERS)
) : Map<Int, MutableMap<Render, ByteArray>> by updates {

    internal fun add(player: Player, render: Render, bytes: ByteArray) {
        if (updates[player.index] == null) {
            updates[player.index] = mutableMapOf()
        }
        updates[player.index]!![render] = bytes
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
