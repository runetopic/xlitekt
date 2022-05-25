package xlitekt.game.tick.benchmark

import com.github.michaelbull.logging.InlineLogger
import org.rsmod.pathfinder.DumbPathFinder
import org.rsmod.pathfinder.Route
import org.rsmod.pathfinder.SmartPathFinder
import org.rsmod.pathfinder.ZoneFlags
import xlitekt.game.actor.chat
import xlitekt.game.actor.hit
import xlitekt.game.actor.npc.NPC
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.render.HitBarType
import xlitekt.game.actor.render.HitType
import xlitekt.game.actor.render.block.createHighDefinitionUpdatesBuffer
import xlitekt.game.actor.render.block.createLowDefinitionUpdatesBuffer
import xlitekt.game.actor.route
import xlitekt.game.actor.spotAnimate
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
    private val smartPathFinder = SmartPathFinder(
        flags = zoneFlags.flags,
        defaultFlag = 0,
        useRouteBlockerFlags = true
    )

    private val dumbPathFinder = DumbPathFinder(
        flags = zoneFlags.flags,
        defaultFlag = 0
    )

    private var tick = 0

    override fun run() {
        val players = world.players()
        val npcs = world.npcs()
        val paths = mutableMapOf<Player, Route>()
        val finders = measureTime {
            val first = players.firstOrNull()
            players.filter { it != first }.forEach {
                paths[it] = smartPathFinder.findPath(
                    srcX = it.location.x,
                    srcY = it.location.z,
                    destX = Random.nextInt(first!!.location.x - 5, first.location.x + 5),
                    destY = Random.nextInt(first.location.z - 5, first.location.z + 5),
                    z = it.location.level
                )
                it.chat(it.rights, 0) { "Hello Xlite." }
                it.spotAnimate { 574 }
                it.hit(HitBarType.DEFAULT, null, HitType.POISON_DAMAGE, 0) { 10 }
            }
        }
        logger.debug { "Pathfinders took $finders for ${players.size} players. [TICK=$tick]" }

        val npcPaths = mutableMapOf<NPC, Route>()
        var count = 0
        val npcFinders = measureTime {
            npcs.forEach {
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
            players.forEach {
                val path = paths[it]
                if (path != null) {
                    it.route { path.coords.map { c -> Location(c.x, c.y, it.location.level) } }
                }
            }
            npcs.forEach {
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
            players.forEach {
                it.invokeAndClearReadPool()
                playerMovementStepsUpdates.add(it, it.processMovement(syncPlayers, it.location))
                highDefinitionUpdates.add(it, it.highDefinitionRenderingBlocks().createHighDefinitionUpdatesBuffer(it))
                lowDefinitionUpdates.add(it, it.lowDefinitionRenderingBlocks().createLowDefinitionUpdatesBuffer())
            }
            npcs.forEach {
                npcMovementStepsUpdates.add(it, it.processMovement(syncPlayers, it.location))
            }
        }
        logger.debug { "Pre tick took $pre for ${players.size} players. [TICK=$tick]" }

        val main = measureTime {
            // Main process.
            players.forEach {
                it.sync(syncPlayers)
            }
            resetSynchronizer()
        }
        logger.debug { "Main tick took $main for ${players.size} players. [TICK=$tick]" }

        tick++
    }
}
