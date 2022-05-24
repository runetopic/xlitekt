package xlitekt.game.tick

import xlitekt.game.actor.movement.MovementStep
import xlitekt.game.actor.npc.NPC
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
        val playerSteps = mutableMapOf<Player, MovementStep>()
        val npcSteps = mutableMapOf<NPC, MovementStep>()
        val highDefinitionUpdates = mutableMapOf<Player, ByteArray>()
        val lowDefinitionUpdates = mutableMapOf<Player, ByteArray>()
        val syncPlayers = players.associateBy(Player::index)

        players.forEach {
            playerSteps[it] = it.processMovement(syncPlayers)
            highDefinitionUpdates[it] = it.highDefinitionRenderingBlocks().createHighDefinitionUpdatesBuffer(it)
            lowDefinitionUpdates[it] = it.lowDefinitionRenderingBlocks().createLowDefinitionUpdatesBuffer()
        }

        npcs.forEach {
            npcSteps[it] = it.processMovement(syncPlayers)
        }

        players.forEach {
            it.sync(syncPlayers, highDefinitionUpdates, lowDefinitionUpdates, playerSteps, npcSteps)
        }
    }
}
