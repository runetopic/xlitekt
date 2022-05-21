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
        val players = world.players.filterNotNull().filter(Player::online)
        val npcs = world.npcs.filterNotNull()
        val playerSteps = mutableMapOf<Player, MovementStep>()
        val npcSteps = mutableMapOf<NPC, MovementStep>()
        val pendingUpdates = mutableMapOf<Player, ByteReadPacket>()
        val cachedUpdates = mutableMapOf<Player, ByteArray>()

        players.forEach {
            playerSteps[it] = it.processMovement()
            pendingUpdates[it] = it.processUpdateBlocks(it.pendingUpdates())
            cachedUpdates[it] = it.cachedUpdates().keys.buildPlayerUpdateBlocks(it, false).readBytes()
        }

        npcs.forEach {
            npcSteps[it] = it.processMovement()
        }

        val syncPlayers = players.associateBy(Player::index)

        players.forEach {
            it.sync(syncPlayers, pendingUpdates, cachedUpdates, playerSteps, npcSteps)
        }
    }
}
