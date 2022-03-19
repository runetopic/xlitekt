package xlitekt.game.world.engine.sync

import com.github.michaelbull.logging.InlineLogger
import io.ktor.utils.io.core.ByteReadPacket
import kotlinx.coroutines.Runnable
import org.rsmod.pathfinder.Route
import org.rsmod.pathfinder.SmartPathFinder
import org.rsmod.pathfinder.ZoneFlags
import xlitekt.game.actor.movement.MovementStep
import xlitekt.game.actor.movement.isValid
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.player.sendRebuildNormal
import xlitekt.game.actor.player.shouldRebuildMap
import xlitekt.game.actor.render.Render
import xlitekt.game.actor.render.block.buildPlayerUpdateBlocks
import xlitekt.game.packet.PlayerInfoPacket
import xlitekt.game.world.World
import xlitekt.game.world.map.location.Location
import xlitekt.shared.inject
import java.util.concurrent.ConcurrentHashMap
import kotlin.random.Random.Default.nextInt
import kotlin.time.measureTime

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

                // Pre player process.
                val steps = ConcurrentHashMap<Player, MovementStep>()
                val updates = ConcurrentHashMap<Player, ByteReadPacket>()
                players.parallelStream().forEach {
                    steps[it] = it.processMovement()
                    updates[it] = it.processUpdateBlocks(it.pendingUpdates())
                }

                // Main player process.
                players.parallelStream().forEach {
                    it.processSync(
                        updates = updates,
                        previousLocations = players.associateWith(Player::previousLocation),
                        locations = players.associateWith(Player::location),
                        steps = steps
                    )
                }
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

    private fun Player.processUpdateBlocks(pending: List<Render>): ByteReadPacket {
        if (pending.isEmpty()) return ByteReadPacket.Empty
        return pending.buildPlayerUpdateBlocks(this)
    }
    private fun Player.processSync(updates: Map<Player, ByteReadPacket>, previousLocations: Map<Player, Location?>, locations: Map<Player, Location>, steps: Map<Player, MovementStep?>) {
        write(PlayerInfoPacket(viewport, updates, previousLocations, locations, steps))
        // write(NPCInfoPacket(viewport, locations))
        flushPool()
        reset()
    }
}
