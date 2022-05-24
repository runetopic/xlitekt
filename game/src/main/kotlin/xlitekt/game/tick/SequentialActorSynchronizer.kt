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
            playerMovementStepsUpdates.add(it, it.processMovement(syncPlayers))
            highDefinitionUpdates.add(it, it.highDefinitionRenderingBlocks().createHighDefinitionUpdatesBuffer(it))
            lowDefinitionUpdates.add(it, it.lowDefinitionRenderingBlocks().createLowDefinitionUpdatesBuffer())
        }

        npcs.forEach {
            npcMovementStepsUpdates.add(it, it.processMovement(syncPlayers))
        }

        players.forEach {
            it.sync(syncPlayers)
        }

        resetSynchronizer()
    }
}
