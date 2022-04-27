package xlitekt.game.tick

import io.ktor.utils.io.core.ByteReadPacket
import java.util.concurrent.ConcurrentHashMap
import xlitekt.game.actor.movement.MovementStep
import xlitekt.game.actor.npc.NPC
import xlitekt.game.actor.player.Player

/**
 * @author Jordan Abraham
 */
class ParallelActorSynchronizer : Synchronizer() {

    override fun run() {
        val players = world.players.filterNotNull().filter(Player::online)
        val npcs = world.npcs.filterNotNull()
        val playerSteps = ConcurrentHashMap<Player, MovementStep>()
        val npcSteps = ConcurrentHashMap<NPC, MovementStep>()
        val updates = ConcurrentHashMap<Player, ByteReadPacket>()

        players.parallelStream().forEach {
            playerSteps[it] = it.processMovement()
            updates[it] = it.processUpdateBlocks(it.pendingUpdates())
        }

        npcs.parallelStream().forEach {
            npcSteps[it] = it.processMovement()
        }

        val previousLocations = players.associateWith(Player::previousLocation)
        val currentLocations = players.associateWith(Player::location)

        players.parallelStream().forEach {
            it.sync(updates, previousLocations, currentLocations, playerSteps, npcSteps)
        }
    }
}
