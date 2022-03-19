package xlitekt.game.world.engine.sync

import com.github.michaelbull.logging.InlineLogger
import io.ktor.utils.io.core.ByteReadPacket
import kotlinx.coroutines.Runnable
import org.rsmod.pathfinder.SmartPathFinder
import org.rsmod.pathfinder.ZoneFlags
import xlitekt.game.actor.movement.MovementStep
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.player.sendRebuildNormal
import xlitekt.game.actor.player.shouldRebuildMap
import xlitekt.game.actor.render.Render
import xlitekt.game.actor.render.block.buildPlayerUpdateBlocks
import xlitekt.game.packet.PlayerInfoPacket
import xlitekt.game.world.World
import xlitekt.game.world.map.location.Location
import xlitekt.shared.inject
import kotlin.time.measureTime

/**
 * @author Jordan Abraham
 */
class PlayerSynchronizer : Runnable {

    private val logger = InlineLogger()
    private val world by inject<World>()

    private val zoneFlags by inject<ZoneFlags>()
    private var tick = 0

    val pf = SmartPathFinder(
        flags = zoneFlags.flags,
        defaultFlag = 0
    )

    override fun run() {
        try {
            val players = world.players.filterNotNull().filter(Player::online)
            val start = measureTime {
                val first = players.firstOrNull()
                players.filter { it != first }.forEach {
                    if (tick > 0 && tick % 15 == 0) {
                        val path = pf.findPath(
                            srcX = it.location.x,
                            srcY = it.location.z,
                            destX = first!!.location.x,
                            destY = first.location.z,
                            z = it.location.level
                        )
                        it.movement.route(path.coords.map { c -> Location(c.x, c.y, it.location.level) })
                    }
                }

                val steps = mutableMapOf<Player, MovementStep?>()
                // Move the player.
                players.forEach { steps[it] = it.processMovement() }
                // Collect player locations.
                val locations = players.associateWith(Player::location)
                // Collect player previous locations.
                val previousLocations = players.associateWith(Player::previousLocation)
                // Collect player pending block updates.
                val pending = players.associateWith(Player::pendingUpdates)
                val updates = mutableMapOf<Player, ByteReadPacket>()
                // Build the player pending block updates.
                players.forEach { it.processUpdateBlocks(pending)?.apply { updates[it] = this } }
                // Sync players.
                players.parallelStream().forEach { it.processSync(updates, previousLocations, locations, steps) }
                // Reset players.
                players.forEach(Player::reset)
                tick++
            }
            logger.debug { "Tick took $start for ${players.size} players. [TICK=$tick]" }
        } catch (e: Exception) {
            logger.error(e) { "Exception caught during player sync." }
        }
    }

    private fun Player.processMovement(): MovementStep? = movement.process(location).also {
        if (shouldRebuildMap()) sendRebuildNormal(false)
    }

    private fun Player.processUpdateBlocks(pending: Map<Player, List<Render>>): ByteReadPacket? {
        val renders = pending[this] ?: return null
        if (renders.isEmpty()) return null
        return renders.buildPlayerUpdateBlocks(this)
    }
    private fun Player.processSync(updates: Map<Player, ByteReadPacket>, previousLocations: Map<Player, Location?>, locations: Map<Player, Location>, steps: Map<Player, MovementStep?>) {
        write(PlayerInfoPacket(viewport, updates, previousLocations, locations, steps))
        // write(NPCInfoPacket(viewport, locations))
        flushPool()
    }
}
