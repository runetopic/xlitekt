package xlitekt.game.loop.task

import io.ktor.utils.io.core.ByteReadPacket
import xlitekt.game.actor.movement.MovementStep
import xlitekt.game.actor.npc.NPC
import xlitekt.game.actor.player.Player
import xlitekt.game.world.World
import xlitekt.shared.inject
import java.util.concurrent.ConcurrentHashMap

/**
 * @author Jordan Abraham
 */
class ParallelSynchronizationTask : Task() {

    private val world by inject<World>()

    override fun run() {
        val players = world.players.filterNotNull().filter(Player::online)
        val npcs = world.npcs.toList().filterNotNull()
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
