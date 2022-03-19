package xlitekt.game.world.engine.sync

import com.github.michaelbull.logging.InlineLogger
import org.rsmod.pathfinder.Route
import io.ktor.utils.io.core.ByteReadPacket
import java.util.concurrent.ConcurrentHashMap
import kotlin.random.Random.Default.nextInt
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
import xlitekt.game.actor.movement.emptyStep
import xlitekt.game.actor.movement.isValid

/**
 * @author Jordan Abraham
 */
class PlayerSynchronizer : Runnable {

    private val logger = InlineLogger()
    private val world by inject<World>()

    private val zoneFlags by inject<ZoneFlags>()
    private var tick = 0

    private val pf = SmartPathFinder(
        flags = zoneFlags.flags,
        defaultFlag = 0
    )

    override fun run() {
        try {
            val players = world.players.filterNotNull().filter(Player::online)
            val start = measureTime {
                val paths = mutableMapOf<Player, Route>()
                val first = players.firstOrNull()
                if (tick > 0 && tick % 10 == 0) {
                    players.filter { it != first }.parallelStream().forEach {
                        paths[it] = SmartPathFinder(
                            flags = zoneFlags.flags,
                            defaultFlag = 0
                        ).findPath(
                            srcX = it.location.x,
                            srcY = it.location.z,
                            destX = nextInt(3210, 3240),
                            destY = nextInt(3210, 3240),
                            z = it.location.level
                        )
                        it.publicChat("Hello Xlite.", 0)
                    }
                }

                players.parallelStream().forEach {
                    val path = paths[it]
                    if (path != null) {
                        it.movement.route(path.coords.map { c -> Location(c.x, c.y, it.location.level) })
                    }
                }

                val steps = ConcurrentHashMap<Player, MovementStep>()
                // Move the player.
                players.parallelStream().forEach { steps[it] = it.processMovement() }
                // Collect player locations.
                val locations = players.associateWith(Player::location)
                // Collect player previous locations.
                val previousLocations = players.associateWith(Player::previousLocation)
                // Collect player pending block updates.
                val pending = players.associateWith(Player::pendingUpdates)
                val updates = ConcurrentHashMap<Player, ByteReadPacket>()
                // Build the player pending block updates.
                players.parallelStream().forEach { updates[it] = it.processUpdateBlocks(pending) }
                // Sync players.
                players.parallelStream().forEach { it.processSync(updates, previousLocations, locations, steps) }
                tick++
            }
            logger.debug { "Tick took $start for ${players.size} players. [TICK=$tick]" }
        } catch (e: Exception) {
            logger.error(e) { "Exception caught during player sync." }
        }
    }

    private fun Player.processMovement(): MovementStep = movement.process(location).also {
        if (it.isValid() && shouldRebuildMap()) sendRebuildNormal(false)
    }

    private fun Player.processUpdateBlocks(pending: Map<Player, List<Render>>): ByteReadPacket {
        val renders = pending[this] ?: return ByteReadPacket.Empty
        if (renders.isEmpty()) return ByteReadPacket.Empty
        return renders.buildPlayerUpdateBlocks(this)
    }
    private fun Player.processSync(updates: Map<Player, ByteReadPacket>, previousLocations: Map<Player, Location?>, locations: Map<Player, Location>, steps: Map<Player, MovementStep?>) {
        write(PlayerInfoPacket(viewport, updates, previousLocations, locations, steps))
        // write(NPCInfoPacket(viewport, locations))
        flushPool()
        reset()
    }
}
