package xlitekt.game.tick

import java.util.Optional
import org.jctools.maps.NonBlockingHashMapLong
import org.jctools.maps.NonBlockingHashSet
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
import xlitekt.game.tick.PlayerInfoUpdates.*
import xlitekt.game.world.World
import xlitekt.game.world.map.zone.Zone
import xlitekt.shared.inject

/**
 * @author Jordan Abraham
 */
abstract class Synchronizer : Runnable {

    protected val world by inject<World>()

    protected fun Player.syncMovement(players: NonBlockingHashMapLong<Player>) {
        processMovement(players)?.let { MovementStepsPlayerUpdates.add(indexL, it) }
    }

    protected fun NPC.syncMovement(players: NonBlockingHashMapLong<Player>) {
        processMovement(players)?.let { MovementStepsNPCUpdates.add(indexL, it) }
    }

    protected fun Player.syncRenderingBlocks() {
        val highDefinition = highDefinitionRenderingBlocks()
        if (highDefinition.isNotEmpty()) HighDefinitionPlayerUpdates.add(indexL, highDefinition.invokeHighDefinitionPlayerRenderingBlocks(this))
        LowDefinitionPlayerUpdates.add(indexL, lowDefinitionRenderingBlocks().invokeLowDefinitionPlayerRenderingBlocks())
        if (highDefinition.isNotEmpty()) AlternativeHighDefinitionPlayerUpdates.add(indexL, alternativeHighDefinitionRenderingBlocks().invokeAlternativeDefinitionPlayerRenderingBlocks())
        AlternativeLowDefinitionPlayerUpdates.add(indexL, alternativeLowDefinitionRenderingBlocks().invokeAlternativeDefinitionPlayerRenderingBlocks())
    }

    protected fun NPC.syncRenderingBlocks() {
        val blocks = highDefinitionRenderingBlocks()
        if (blocks.isEmpty()) return
        HighDefinitionNPCUpdates.add(indexL, blocks.invokeHighDefinitionNPCRenderingBlocks())
        resetDefinitionRenderingBlocks()
    }

    protected fun Player.syncZones() {
        zones().filter(Zone::updating).forEach {
            ZoneUpdates.add(it.invokeUpdateRequests(this))
        }
    }

    protected fun Player.syncClient(players: NonBlockingHashMapLong<Player>) {
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
        // Zones
        ZoneUpdates.clear()
    }
}

internal object ZoneUpdates : NonBlockingHashSet<Zone>()

internal sealed class PlayerInfoUpdates<T : Any> : NonBlockingHashMapLong<Optional<T>>(World.MAX_PLAYERS) {

    object HighDefinitionPlayerUpdates : PlayerInfoUpdates<ByteArray>()
    object LowDefinitionPlayerUpdates : PlayerInfoUpdates<ByteArray>()
    object AlternativeHighDefinitionPlayerUpdates : PlayerInfoUpdates<ByteArray>()
    object AlternativeLowDefinitionPlayerUpdates : PlayerInfoUpdates<ByteArray>()
    object MovementStepsPlayerUpdates : PlayerInfoUpdates<MovementStep>()

    fun add(index: Long, update: T) {
        put(index, Optional.of(update))
    }
}

internal sealed class NPCInfoUpdates<T : Any> : NonBlockingHashMapLong<Optional<T>>() {

    object HighDefinitionNPCUpdates : NPCInfoUpdates<ByteArray>()
    object MovementStepsNPCUpdates : NPCInfoUpdates<MovementStep>()

    fun add(index: Long, update: T) {
        put(index, Optional.of(update))
    }
}
