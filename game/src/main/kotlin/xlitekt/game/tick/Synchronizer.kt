package xlitekt.game.tick

import org.jctools.maps.NonBlockingHashMapLong
import xlitekt.game.actor.movement.MovementStep
import xlitekt.game.actor.npc.NPC
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.processInteractions
import xlitekt.game.actor.render.block.invokeAlternativeDefinitionPlayerRenderingBlocks
import xlitekt.game.actor.render.block.invokeHighDefinitionNPCRenderingBlocks
import xlitekt.game.actor.render.block.invokeHighDefinitionPlayerRenderingBlocks
import xlitekt.game.actor.render.block.invokeLowDefinitionPlayerRenderingBlocks
import xlitekt.game.packet.NPCInfoPacket
import xlitekt.game.packet.PlayerInfoPacket
import xlitekt.game.world.World
import xlitekt.game.world.map.zone.Zone
import xlitekt.shared.inject

/**
 * @author Jordan Abraham
 */
abstract class Synchronizer : Runnable {

    protected val world by inject<World>()

    private val zoneUpdates = HashSet<Zone>()

    private val highDefinitionPlayerUpdates = arrayOfNulls<ByteArray?>(World.MAX_PLAYERS)
    private val lowDefinitionPlayerUpdates = arrayOfNulls<ByteArray?>(World.MAX_PLAYERS)
    private val alternativeHighDefinitionPlayerUpdates = arrayOfNulls<ByteArray?>(World.MAX_PLAYERS)
    private val alternativeLowDefinitionPlayerUpdates = arrayOfNulls<ByteArray?>(World.MAX_PLAYERS)
    private val movementStepsPlayerUpdates = arrayOfNulls<MovementStep?>(World.MAX_PLAYERS)

    private val highDefinitionNPCUpdates = arrayOfNulls<ByteArray?>(World.MAX_NPCS)
    private val movementStepsNPCUpdates = arrayOfNulls<MovementStep?>(World.MAX_NPCS)

    protected fun Player.syncMovement(players: NonBlockingHashMapLong<Player>) {
        processInteractions(players)?.let { movementStepsPlayerUpdates[index] = it }
    }

    protected fun NPC.syncMovement(players: NonBlockingHashMapLong<Player>) {
        processInteractions(players)?.let { movementStepsPlayerUpdates[index] = it }
    }

    protected fun Player.syncRenderingBlocks() {
        val highDefinition = highDefinitionRenderingBlocks()
        if (highDefinition.isNotEmpty()) highDefinitionPlayerUpdates[index] = highDefinition.invokeHighDefinitionPlayerRenderingBlocks(this)
        lowDefinitionPlayerUpdates[index] = lowDefinitionRenderingBlocks().invokeLowDefinitionPlayerRenderingBlocks()
        if (highDefinition.isNotEmpty()) alternativeHighDefinitionPlayerUpdates[index] = alternativeHighDefinitionRenderingBlocks().invokeAlternativeDefinitionPlayerRenderingBlocks()
        alternativeLowDefinitionPlayerUpdates[index] = alternativeLowDefinitionRenderingBlocks().invokeAlternativeDefinitionPlayerRenderingBlocks()
    }

    protected fun NPC.syncRenderingBlocks() {
        val blocks = highDefinitionRenderingBlocks()
        if (blocks.isEmpty()) return
        highDefinitionNPCUpdates[index] = blocks.invokeHighDefinitionNPCRenderingBlocks()
        resetDefinitionRenderingBlocks()
    }

    protected fun Player.syncZones() {
        zones().filter(Zone::updating).forEach {
            zoneUpdates.add(it.invokeUpdateRequests(this))
        }
    }

    protected fun Player.syncClient(players: NonBlockingHashMapLong<Player>) {
        write(
            PlayerInfoPacket(
                players = players,
                viewport = viewport,
                highDefinitionUpdates = highDefinitionPlayerUpdates,
                lowDefinitionUpdates = lowDefinitionPlayerUpdates,
                alternativeHighDefinitionUpdates = alternativeHighDefinitionPlayerUpdates,
                alternativeLowDefinitionUpdates = alternativeLowDefinitionPlayerUpdates,
                movementStepsUpdates = movementStepsPlayerUpdates
            )
        )
        write(
            NPCInfoPacket(
                viewport = viewport,
                highDefinitionUpdates = highDefinitionNPCUpdates,
                movementStepsUpdates = movementStepsNPCUpdates
            )
        )
        invokeAndClearWritePool()
        resetDefinitionRenderingBlocks()
    }

    protected fun resetSynchronizer() {
        // Player
        highDefinitionPlayerUpdates.fill(null, 0)
        lowDefinitionPlayerUpdates.fill(null, 0)
        alternativeHighDefinitionPlayerUpdates.fill(null, 0)
        alternativeLowDefinitionPlayerUpdates.fill(null, 0)
        movementStepsPlayerUpdates.fill(null, 0)
        // NPC
        highDefinitionNPCUpdates.fill(null, 0)
        movementStepsNPCUpdates.fill(null, 0)
        // Zones
        zoneUpdates.forEach(Zone::finalizeUpdateRequests)
        zoneUpdates.clear()
    }
}
