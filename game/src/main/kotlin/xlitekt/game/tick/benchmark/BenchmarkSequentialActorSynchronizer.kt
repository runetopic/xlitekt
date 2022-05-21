package xlitekt.game.tick.benchmark

import com.github.michaelbull.logging.InlineLogger
import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readBytes
import org.rsmod.pathfinder.DumbPathFinder
import org.rsmod.pathfinder.Route
import org.rsmod.pathfinder.SmartPathFinder
import org.rsmod.pathfinder.ZoneFlags
import xlitekt.game.actor.chat
import xlitekt.game.actor.movement.MovementStep
import xlitekt.game.actor.npc.NPC
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.render.block.buildPlayerUpdateBlocks
import xlitekt.game.tick.Synchronizer
import xlitekt.game.world.map.location.Location
import xlitekt.shared.inject
import kotlin.random.Random
import kotlin.time.measureTime

/**
 * @author Jordan Abraham
 */
class BenchmarkSequentialActorSynchronizer : Synchronizer() {

    private val logger = InlineLogger()
    private val zoneFlags by inject<ZoneFlags>()

    private var tick = 0

    override fun run() {
        val players = world.players()
        val npcs = world.npcs()
        val paths = mutableMapOf<Player, Route>()
        val finders = measureTime {
            val first = players.firstOrNull()
            players.filter { it != first }.forEach {
                paths[it] = SmartPathFinder(
                    flags = zoneFlags.flags,
                    defaultFlag = 0,
                    useRouteBlockerFlags = true
                ).findPath(
                    srcX = it.location.x,
                    srcY = it.location.z,
                    destX = Random.nextInt(first!!.location.x - 5, first.location.x + 5),
                    destY = Random.nextInt(first.location.z - 5, first.location.z + 5),
                    z = it.location.level
                )
                it.chat(it.rights, 0) { "Hello Xlite." }
            }
        }
        logger.debug { "Pathfinders took $finders for ${players.size} players. [TICK=$tick]" }

        val npcPaths = mutableMapOf<NPC, Route>()
        var count = 0
        val npcFinders = measureTime {
            npcs.forEach {
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
            players.forEach {
                val path = paths[it]
                if (path != null) {
                    it.route(path.coords.map { c -> Location(c.x, c.y, it.location.level) })
                }
            }
            npcs.forEach {
                val path = npcPaths[it]
                if (path != null) {
                    it.route(path.coords.map { c -> Location(c.x, c.y, it.location.level) })
                }
            }
        }
        logger.debug { "Movement routing took $moves for all entities. [TICK=$tick]" }

        // Pre process.
        val playerSteps = mutableMapOf<Player, MovementStep>()
        val npcSteps = mutableMapOf<NPC, MovementStep>()
        val pendingUpdates = mutableMapOf<Player, ByteReadPacket>()
        val cachedUpdates = mutableMapOf<Player, ByteArray>()
        val syncPlayers = players.associateBy(Player::index)

        val pre = measureTime {
            players.forEach {
                playerSteps[it] = it.processMovement(syncPlayers)
                pendingUpdates[it] = it.processUpdateBlocks(it.pendingUpdates())
                cachedUpdates[it] = it.cachedUpdates().keys.buildPlayerUpdateBlocks(it, false).readBytes()
            }
            npcs.forEach {
                npcSteps[it] = it.processMovement(syncPlayers)
            }
        }
        logger.debug { "Pre tick took $pre for ${players.size} players. [TICK=$tick]" }

        val main = measureTime {
            // Main process.
            players.forEach {
                it.sync(syncPlayers, pendingUpdates, cachedUpdates, playerSteps, npcSteps)
            }
        }
        logger.debug { "Main tick took $main for ${players.size} players. [TICK=$tick]" }

        tick++
    }
}
