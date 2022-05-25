package xlitekt.game.tick

import xlitekt.game.actor.player.Player
import xlitekt.game.actor.render.block.createHighDefinitionUpdatesBuffer
import xlitekt.game.actor.render.block.createLowDefinitionUpdatesBuffer

/**
 * @author Jordan Abraham
 */
class SequentialActorSynchronizer : Synchronizer() {

    override fun run() {
        val players = world.players()
        val npcs = world.npcs()
        val syncPlayers = players.associateBy(Player::index)

        players.forEach {
            it.invokeAndClearReadPool()
            playerMovementStepsUpdates.add(it, it.processMovement(syncPlayers, it.location))
            highDefinitionUpdates.add(it, it.highDefinitionRenderingBlocks().createHighDefinitionUpdatesBuffer(it))
            lowDefinitionUpdates.add(it, it.lowDefinitionRenderingBlocks().createLowDefinitionUpdatesBuffer())
        }

        npcs.forEach {
            npcMovementStepsUpdates.add(it, it.processMovement(syncPlayers, it.location))
        }

        players.forEach {
            it.sync(syncPlayers)
        }

        resetSynchronizer()
    }
}
