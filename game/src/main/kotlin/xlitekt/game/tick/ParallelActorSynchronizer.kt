package xlitekt.game.tick

import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readBytes
import xlitekt.game.actor.movement.MovementStep
import xlitekt.game.actor.npc.NPC
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.render.block.buildPlayerUpdateBlocks
import java.util.concurrent.ConcurrentHashMap

/**
 * @author Jordan Abraham
 */
class ParallelActorSynchronizer : Synchronizer() {

    override fun run() {
        val players = world.players.filterNotNull().filter(Player::online)
        val npcs = world.npcs.filterNotNull()
        val playerSteps = ConcurrentHashMap<Player, MovementStep>()
        val npcSteps = ConcurrentHashMap<NPC, MovementStep>()
        val pendingUpdates = ConcurrentHashMap<Player, ByteReadPacket>()
        val cachedUpdates = ConcurrentHashMap<Player, ByteArray>()

        players.parallelStream().forEach {
            playerSteps[it] = it.processMovement()
            pendingUpdates[it] = it.processUpdateBlocks(it.pendingUpdates())
            cachedUpdates[it] = it.cachedUpdates().keys.buildPlayerUpdateBlocks(it, false).readBytes()
        }

        npcs.parallelStream().forEach {
            npcSteps[it] = it.processMovement()
        }

        val syncPlayers = players.associateBy(Player::index)

        players.parallelStream().forEach {
            it.sync(syncPlayers, pendingUpdates, cachedUpdates, playerSteps, npcSteps)
        }
    }
}
