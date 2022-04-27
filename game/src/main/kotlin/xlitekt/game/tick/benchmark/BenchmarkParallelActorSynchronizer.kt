package xlitekt.game.tick.benchmark

import com.github.michaelbull.logging.InlineLogger
import io.ktor.utils.io.core.ByteReadPacket
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.ConcurrentHashMap
import kotlin.random.Random
import kotlin.time.measureTime
import org.rsmod.pathfinder.DumbPathFinder
import org.rsmod.pathfinder.PathFinder
import org.rsmod.pathfinder.Route
import org.rsmod.pathfinder.SmartPathFinder
import org.rsmod.pathfinder.ZoneFlags
import xlitekt.game.actor.movement.MovementStep
import xlitekt.game.actor.npc.NPC
import xlitekt.game.actor.player.Player
import xlitekt.game.tick.Synchronizer
import xlitekt.game.world.map.location.Location
import xlitekt.shared.inject

/**
 * @author Jordan Abraham
 */
class BenchmarkParallelActorSynchronizer : Synchronizer() {

    private val logger = InlineLogger()
    private val zoneFlags by inject<ZoneFlags>()
    private val threads = Runtime.getRuntime().availableProcessors()
    private val queue: ArrayBlockingQueue<PathFinder> = ArrayBlockingQueue(
        threads, false,
        buildList {
            repeat(threads) {
                add(
                    SmartPathFinder(
                        flags = zoneFlags.flags,
                        defaultFlag = 0,
                        useRouteBlockerFlags = true
                    )
                )
            }
        }
    )

    private var tick = 0

    override fun run() {
        val players = world.players.filterNotNull().filter(Player::online)
        val npcs = world.npcs.filterNotNull()
        val paths = ConcurrentHashMap<Player, Route>()
        val finders = measureTime {
            val first = players.firstOrNull()
            players.filter { it != first }.parallelStream().forEach {
                val pf = queue.take()
                paths[it] = pf.findPath(
                    srcX = it.location.x,
                    srcY = it.location.z,
                    destX = Random.nextInt(3210, 3240),
                    destY = Random.nextInt(3210, 3240),
                    z = it.location.level
                )
                queue.put(pf)
                it.publicChat("Hello Xlite.", 0, it.rights)
            }
        }
        logger.debug { "Pathfinders took $finders for ${players.size} players. [TICK=$tick]" }

        val npcPaths = ConcurrentHashMap<NPC, Route>()
        var count = 0
        val npcFinders = measureTime {
            npcs.parallelStream().forEach {
                npcPaths[it] = DumbPathFinder(
                    flags = zoneFlags.flags,
                    defaultFlag = 0
                ).findPath(
                    srcX = it.location.x,
                    srcY = it.location.z,
                    destX = Random.nextInt(it.location.x - 5, it.location.x + 5),
                    destY = Random.nextInt(it.location.z - 5, it.location.z + 5),
                    z = it.location.level
                )
                count++
            }
        }
        logger.debug { "Pathfinders took $npcFinders for $count npcs. [TICK=$tick]" }

        val moves = measureTime {
            players.parallelStream().forEach {
                val path = paths[it]
                if (path != null) {
                    it.movement.route(path.coords.map { c -> Location(c.x, c.y, it.location.level) })
                }
            }
            npcs.parallelStream().forEach {
                val path = npcPaths[it]
                if (path != null) {
                    it.movement.route(path.coords.map { c -> Location(c.x, c.y, it.location.level) })
                }
            }
        }
        logger.debug { "Movement routing took $moves for all entities. [TICK=$tick]" }

        // Pre process.
        val playerSteps = ConcurrentHashMap<Player, MovementStep>()
        val npcSteps = ConcurrentHashMap<NPC, MovementStep>()
        val updates = ConcurrentHashMap<Player, ByteReadPacket>()
        val pre = measureTime {
            players.parallelStream().forEach {
                playerSteps[it] = it.processMovement()
                updates[it] = it.processUpdateBlocks(it.pendingUpdates())
            }
            npcs.parallelStream().forEach {
                npcSteps[it] = it.processMovement()
            }
        }
        logger.debug { "Pre tick took $pre for ${players.size} players. [TICK=$tick]" }

        val main = measureTime {
            // Main process.
            val previousLocations = players.associateWith(Player::previousLocation)
            val currentLocations = players.associateWith(Player::location)
            players.parallelStream().forEach {
                it.sync(updates, previousLocations, currentLocations, playerSteps, npcSteps)
            }
        }
        logger.debug { "Main tick took $main for ${players.size} players. [TICK=$tick]" }

        tick++
    }
}
