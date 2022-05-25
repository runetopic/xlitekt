package xlitekt.game.tick

import xlitekt.game.actor.player.Player
import xlitekt.game.actor.render.block.createHighDefinitionUpdatesBuffer
import xlitekt.game.actor.render.block.createLowDefinitionUpdatesBuffer

/**
 * @author Jordan Abraham
 */
class ParallelActorSynchronizer : Synchronizer() {

    override fun run() {
        val players = world.players()
        val npcs = world.npcs()
        val syncPlayers = players.associateBy(Player::index)

        players.parallelStream().forEach {
            it.invokeAndClearReadPool()
            playerMovementStepsUpdates.add(it, it.processMovement(syncPlayers, it.location))
            highDefinitionUpdates.add(it, it.highDefinitionRenderingBlocks().createHighDefinitionUpdatesBuffer(it))
            lowDefinitionUpdates.add(it, it.lowDefinitionRenderingBlocks().createLowDefinitionUpdatesBuffer())
        }

        npcs.parallelStream().forEach {
            npcMovementStepsUpdates.add(it, it.processMovement(syncPlayers, it.location))
        }

        players.parallelStream().forEach {
            it.sync(syncPlayers)
        }

        resetSynchronizer()
    }
}
