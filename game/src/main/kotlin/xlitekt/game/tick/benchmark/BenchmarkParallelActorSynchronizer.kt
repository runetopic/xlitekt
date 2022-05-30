package xlitekt.game.tick.benchmark

import com.github.michaelbull.logging.InlineLogger
import org.rsmod.pathfinder.DumbPathFinder
import org.rsmod.pathfinder.PathFinder
import org.rsmod.pathfinder.Route
import org.rsmod.pathfinder.SmartPathFinder
import org.rsmod.pathfinder.ZoneFlags
import xlitekt.game.actor.chat
import xlitekt.game.actor.hit
import xlitekt.game.actor.npc.NPC
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.render.HitBar
import xlitekt.game.actor.render.HitType
import xlitekt.game.actor.route
import xlitekt.game.actor.spotAnimate
import xlitekt.game.tick.Synchronizer
import xlitekt.game.world.map.location.Location
import xlitekt.game.world.map.zone.Zone
import xlitekt.shared.inject
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.ConcurrentHashMap
import kotlin.random.Random
import kotlin.time.measureTime

/**
 * @author Jordan Abraham
 */
class BenchmarkParallelActorSynchronizer : Synchronizer() {

    private val logger = InlineLogger()
    private val zoneFlags by inject<ZoneFlags>()
    private val threads = Runtime.getRuntime().availableProcessors()
    private val smartPathFinders: ArrayBlockingQueue<PathFinder> = ArrayBlockingQueue(
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

    private val dumbPathFinder = DumbPathFinder(
        flags = zoneFlags.flags,
        defaultFlag = 0
    )

    private var tick = 0

    override fun run() {
        val players = world.players()
        val npcs = world.npcs()
        val zones = players.flatMap(Player::zones).distinct().filter(Zone::updating)
        val paths = ConcurrentHashMap<Player, Route>()
        val finders = measureTime {
            val first = players.firstOrNull()
            players.filter { it != first }.parallelStream().forEach {
                val pf = smartPathFinders.take()
                paths[it] = pf.findPath(
                    srcX = it.location.x,
                    srcY = it.location.z,
                    destX = Random.nextInt(first!!.location.x - 5, first.location.x + 5),
                    destY = Random.nextInt(first.location.z - 5, first.location.z + 5),
                    z = it.location.level
                )
                smartPathFinders.put(pf)
                it.chat(it.rights, 0) { "Hello Xlite." }
                it.spotAnimate { 574 }
                it.hit(HitBar.DEFAULT, null, HitType.values().random(), 0) { Random.nextInt(1, 127) }
            }
        }
        logger.debug { "Pathfinders took $finders for ${players.size} players. [TICK=$tick]" }

        val npcPaths = ConcurrentHashMap<NPC, Route>()
        var count = 0
        val npcFinders = measureTime {
            npcs.parallelStream().forEach {
                npcPaths[it] = dumbPathFinder.findPath(
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
                    it.route { path.coords.map { c -> Location(c.x, c.y, it.location.level) } }
                }
            }
            npcs.parallelStream().forEach {
                val path = npcPaths[it]
                if (path != null) {
                    it.route { path.coords.map { c -> Location(c.x, c.y, it.location.level) } }
                }
            }
        }
        logger.debug { "Movement routing took $moves for all entities. [TICK=$tick]" }

        // Pre process.
        val syncPlayers = players.associateBy(Player::index)

        val pre = measureTime {
            players.parallelStream().forEach {
                it.invokeAndClearReadPool()
                it.syncMovement(syncPlayers)
                it.syncRenderingBlocks()
            }

            npcs.parallelStream().forEach {
                it.syncMovement(syncPlayers)
                it.syncRenderingBlocks()
            }
        }
        logger.debug { "Pre tick took $pre for ${players.size} players. [TICK=$tick]" }

        val zonesTime = measureTime {
            zones.parallelStream().forEach(Zone::update)
        }
        logger.debug { "Zones took $zonesTime to update. [TICK=$tick]" }

        val main = measureTime {
            // Main process.
            players.parallelStream().forEach {
                it.syncClient(syncPlayers)
            }
            resetSynchronizer()
        }
        logger.debug { "Main tick took $main for ${players.size} players. [TICK=$tick]" }

        tick++
    }
}
