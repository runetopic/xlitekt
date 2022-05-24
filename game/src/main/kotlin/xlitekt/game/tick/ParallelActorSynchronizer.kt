package xlitekt.game.tick

import xlitekt.game.actor.movement.MovementStep
import xlitekt.game.actor.npc.NPC
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.render.block.createHighDefinitionUpdatesBuffer
import xlitekt.game.actor.render.block.createLowDefinitionUpdatesBuffer
import java.util.Optional
import java.util.concurrent.ConcurrentHashMap

/**
 * @author Jordan Abraham
 */
class ParallelActorSynchronizer : Synchronizer() {

    override fun run() {
        val players = world.players()
        val npcs = world.npcs()
        val playerSteps = ConcurrentHashMap<Player, Optional<MovementStep>>()
        val npcSteps = ConcurrentHashMap<NPC, Optional<MovementStep>>()
        val highDefinitionUpdates = ConcurrentHashMap<Player, Optional<ByteArray>>()
        val lowDefinitionUpdates = ConcurrentHashMap<Player, Optional<ByteArray>>()
        val syncPlayers = players.associateBy(Player::index)

        players.parallelStream().forEach {
            playerSteps[it] = Optional.ofNullable(it.processMovement(syncPlayers))
            highDefinitionUpdates[it] = Optional.ofNullable(it.highDefinitionRenderingBlocks().createHighDefinitionUpdatesBuffer(it))
            lowDefinitionUpdates[it] = Optional.ofNullable(it.lowDefinitionRenderingBlocks().createLowDefinitionUpdatesBuffer())
        }

        npcs.parallelStream().forEach {
            npcSteps[it] = Optional.ofNullable(it.processMovement(syncPlayers))
        }

        players.parallelStream().forEach {
            it.sync(syncPlayers, highDefinitionUpdates, lowDefinitionUpdates, playerSteps, npcSteps)
        }
    }
}
