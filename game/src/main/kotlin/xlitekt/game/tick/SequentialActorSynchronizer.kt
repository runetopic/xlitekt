package xlitekt.game.tick

import xlitekt.game.actor.movement.MovementStep
import xlitekt.game.actor.npc.NPC
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.render.block.createHighDefinitionUpdatesBuffer
import xlitekt.game.actor.render.block.createLowDefinitionUpdatesBuffer
import java.util.Optional

/**
 * @author Jordan Abraham
 */
class SequentialActorSynchronizer : Synchronizer() {

    override fun run() {
        val players = world.players()
        val npcs = world.npcs()
        val playerSteps = mutableMapOf<Player, Optional<MovementStep>>()
        val npcSteps = mutableMapOf<NPC, Optional<MovementStep>>()
        val highDefinitionUpdates = mutableMapOf<Player, Optional<ByteArray>>()
        val lowDefinitionUpdates = mutableMapOf<Player, Optional<ByteArray>>()
        val syncPlayers = players.associateBy(Player::index)

        players.forEach {
            playerSteps[it] = Optional.ofNullable(it.processMovement(syncPlayers))
            highDefinitionUpdates[it] = Optional.ofNullable(it.highDefinitionRenderingBlocks().createHighDefinitionUpdatesBuffer(it))
            lowDefinitionUpdates[it] = Optional.ofNullable(it.lowDefinitionRenderingBlocks().createLowDefinitionUpdatesBuffer())
        }

        npcs.forEach {
            npcSteps[it] = Optional.ofNullable(it.processMovement(syncPlayers))
        }

        players.forEach {
            it.sync(syncPlayers, highDefinitionUpdates, lowDefinitionUpdates, playerSteps, npcSteps)
        }
    }
}
