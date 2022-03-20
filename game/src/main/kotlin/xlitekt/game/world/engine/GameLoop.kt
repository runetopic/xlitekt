package xlitekt.game.world.engine

import com.github.michaelbull.logging.InlineLogger
import io.ktor.utils.io.core.ByteReadPacket
import kotlinx.coroutines.Runnable
import org.rsmod.pathfinder.DumbPathFinder
import org.rsmod.pathfinder.PathFinder
import org.rsmod.pathfinder.Route
import org.rsmod.pathfinder.SmartPathFinder
import org.rsmod.pathfinder.ZoneFlags
import xlitekt.game.actor.Actor
import xlitekt.game.actor.movement.MovementStep
import xlitekt.game.actor.movement.isValid
import xlitekt.game.actor.npc.NPC
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
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.ConcurrentHashMap
import kotlin.random.Random.Default.nextInt
import kotlin.time.measureTime

/**
 * @author Jordan Abraham
 */
class GameLoop : Runnable {

    private val logger = InlineLogger()
    private val world by inject<World>()
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
        try {
            val players = world.players.filterNotNull().filter(Player::online)
            val npcs = world.npcs.toList().filterNotNull()

            val start = measureTime {
                val paths = ConcurrentHashMap<Player, Route>()
                val finders = measureTime {
                    val first = players.firstOrNull()
                    players.filter { it != first }.parallelStream().forEach {
                        val pf = queue.take()
                        paths[it] = pf.findPath(
                            srcX = it.location.x,
                            srcY = it.location.z,
                            destX = nextInt(3210, 3240),
                            destY = nextInt(3210, 3240),
                            z = it.location.level
                        )
                        queue.put(pf)
                        it.publicChat("Hello Xlite.", 0)
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
                            destX = nextInt(it.location.x - 5, it.location.x + 5),
                            destY = nextInt(it.location.z - 5, it.location.z + 5),
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
                        it.processSync(updates, previousLocations, currentLocations, playerSteps, npcSteps)
                    }
                }
                logger.debug { "Main tick took $main for ${players.size} players. [TICK=$tick]" }
            }
            logger.debug { "Entire tick took $start for ${players.size} players. [TICK=$tick]" }
            tick++
        } catch (e: Exception) {
            logger.error(e) { "Exception caught during player sync." }
        }
    }

    private fun Actor.processMovement(): MovementStep = movement.process(location).also {
        if (this is Player) {
            if (it.isValid() && shouldRebuildMap()) sendRebuildNormal(false)
        }
    }

    private fun Player.processUpdateBlocks(pending: List<Render>): ByteReadPacket {
        if (pending.isEmpty()) return ByteReadPacket.Empty
        return pending.buildPlayerUpdateBlocks(this)
    }

    private fun Player.processSync(
        updates: Map<Player, ByteReadPacket>,
        previousLocations: Map<Player, Location?>,
        locations: Map<Player, Location>,
        playerSteps: Map<Player, MovementStep?>,
        npcSteps: ConcurrentHashMap<NPC, MovementStep>
    ) {
        write(PlayerInfoPacket(viewport, updates, previousLocations, locations, playerSteps))
        write(NPCInfoPacket(viewport, locations, npcSteps))
        flushPool()
        reset()
    }
}
