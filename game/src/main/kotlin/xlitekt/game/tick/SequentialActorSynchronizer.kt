package xlitekt.game.tick

import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readBytes
import xlitekt.game.actor.movement.MovementStep
import xlitekt.game.actor.npc.NPC
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.render.block.buildPlayerUpdateBlocks

/**
 * @author Jordan Abraham
 */
class SequentialActorSynchronizer : Synchronizer() {

    override fun run() {
        val players = world.players()
        val npcs = world.npcs()
        val playerSteps = mutableMapOf<Player, MovementStep>()
        val npcSteps = mutableMapOf<NPC, MovementStep>()
        val pendingUpdates = mutableMapOf<Player, ByteReadPacket>()
        val cachedUpdates = mutableMapOf<Player, ByteArray>()
        val syncPlayers = players.associateBy(Player::index)

        players.forEach {
            playerSteps[it] = it.processMovement(syncPlayers)
            pendingUpdates[it] = it.processUpdateBlocks(it.pendingUpdates())
            cachedUpdates[it] = it.cachedUpdates().keys.buildPlayerUpdateBlocks(it, false).readBytes()
        }

        npcs.forEach {
            npcSteps[it] = it.processMovement(syncPlayers)
        }

        players.forEach {
            it.sync(syncPlayers, pendingUpdates, cachedUpdates, playerSteps, npcSteps)
        }
    }
}
