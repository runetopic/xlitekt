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

    override fun run() {
        val start = System.nanoTime()
        val players = world.players.filterNotNull().filter(Player::online)
        val steps = mutableMapOf<Player, MovementStep?>()
        // Move the player.
        players.forEach { steps[it] = it.processMovement() }
        // Collect player locations.
        val locations = players.associateWith(Player::location)
        // Collect player previous locations.
        val previousLocations = players.associateWith(Player::previousLocation)
        // Collect player teleports.
        val teleports = players.associateWith(Player::teleported)
        // Collect player pending block updates.
        val pending = players.associateWith { it.pendingUpdates().toList() }
        val updates = mutableMapOf<Player, ByteReadPacket>()
        // Build the player pending block updates.
        players.forEach { it.processUpdateBlocks(pending)?.apply { updates[it] = this } }
        // Sync players.
        players.parallelStream().forEach { it.processSync(updates, previousLocations, locations, teleports, steps) }
        // Reset players.
        players.forEach(Player::reset)

        logger.debug { "Synchronization took ${TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start)}ms for ${players.size} players." }
    }

    private fun Player.processMovement(): MovementStep? = movement.process(location).also {
        if (shouldRebuildMap()) sendRebuildNormal(false)
    }

    private fun Player.processUpdateBlocks(pending: Map<Player, List<Render>>): ByteReadPacket? = pending[this]?.buildPlayerUpdateBlocks(this)
    private fun Player.processSync(updates: Map<Player, ByteReadPacket>, previousLocations: Map<Player, Location?>, locations: Map<Player, Location>, teleports: Map<Player, Boolean>, steps: Map<Player, MovementStep?>) {
        write(PlayerInfoPacket(viewport, updates, previousLocations, locations, teleports, steps))
        write(NPCInfoPacket(viewport))
        flushPool()
    }
}
