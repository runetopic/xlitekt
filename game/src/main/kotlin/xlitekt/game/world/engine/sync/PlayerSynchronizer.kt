package xlitekt.game.world.engine.sync

import com.github.michaelbull.logging.InlineLogger
import io.ktor.utils.io.core.ByteReadPacket
import kotlinx.coroutines.Runnable
import xlitekt.game.actor.movement.MovementStep
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.player.sendRebuildNormal
import xlitekt.game.actor.player.shouldRebuildMap
import xlitekt.game.actor.render.Render
import xlitekt.game.actor.render.block.buildPlayerUpdateBlocks
import xlitekt.game.packet.NPCInfoPacket
import xlitekt.game.packet.PlayerInfoPacket
import xlitekt.game.world.World
import xlitekt.game.world.map.location.Location
import xlitekt.shared.inject
import java.util.concurrent.TimeUnit

/**
 * @author Jordan Abraham
 */
class PlayerSynchronizer : Runnable {

    private val logger = InlineLogger()
    private val world by inject<World>()
    private var tick = 0

    override fun run() {
        val start = System.nanoTime()
        val players = world.players.filterNotNull().filter(Player::online)

        val steps = mutableMapOf<Player, MovementStep?>()
        players.forEach {
            steps[it] = it.processMovement()
        }

        val pending = players.associateWith { it.pendingUpdates().toList() }
        val updates = mutableMapOf<Player, ByteReadPacket>()
        players.forEach {
            it.processUpdateBlocks(pending)?.apply { updates[it] = this }
        }

        val locations = players.associateWith(Player::location)
        players.parallelStream().forEach {
            it.processSync(updates, locations, steps)
        }

        players.forEach(Player::reset)
        logger.debug { "Synchronization took ${TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start)}ms for ${players.size} players." }
        tick++
    }

    private fun Player.processMovement(): MovementStep? = movement.process().also {
        if (shouldRebuildMap()) sendRebuildNormal(false)
    }

    private fun Player.processUpdateBlocks(pending: Map<Player, List<Render>>): ByteReadPacket? = pending[this]?.buildPlayerUpdateBlocks(this)
    private fun Player.processSync(updates: Map<Player, ByteReadPacket>, locations: Map<Player, Location>, steps: Map<Player, MovementStep?>) {
        write(PlayerInfoPacket(viewport, updates, locations, steps))
        write(NPCInfoPacket(viewport))
        flushPool()
    }
}
