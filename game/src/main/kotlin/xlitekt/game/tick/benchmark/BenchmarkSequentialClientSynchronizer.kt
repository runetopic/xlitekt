package xlitekt.game.tick.benchmark

import com.github.michaelbull.logging.InlineLogger
import it.unimi.dsi.fastutil.ints.IntArrayList
import org.rsmod.pathfinder.DumbPathFinder
import org.rsmod.pathfinder.SmartPathFinder
import org.rsmod.pathfinder.ZoneFlags
import xlitekt.game.actor.chat
import xlitekt.game.actor.hit
import xlitekt.game.actor.render.HitBar
import xlitekt.game.actor.render.HitType
import xlitekt.game.actor.route
import xlitekt.game.actor.spotAnimate
import xlitekt.game.tick.Synchronizer
import xlitekt.game.tick.ZoneUpdates
import xlitekt.game.world.map.Location
import xlitekt.game.world.map.zone.Zone
import xlitekt.shared.inject
import kotlin.random.Random
import kotlin.time.measureTime

/**
 * @author Jordan Abraham
 */
class BenchmarkSequentialClientSynchronizer : Synchronizer() {

    private val logger = InlineLogger()
    private val zoneFlags by inject<ZoneFlags>()
    private val smartPathFinder = SmartPathFinder(flags = zoneFlags.flags, defaultFlag = 0)

    private val dumbPathFinder = DumbPathFinder(
        flags = zoneFlags.flags,
        defaultFlag = 0
    )

    private var tick = 0

    override fun run() {
        tick++
        val players = world.players()
        val syncPlayers = world.playersMapped()

        val playerFindersTime = measureTime {
            val first = players.firstOrNull()
            players.filter { it != first }.forEach {
                val path = smartPathFinder.findPath(
                    srcX = it.location.x,
                    srcY = it.location.z,
                    destX = Random.nextInt(first!!.location.x - 5, first.location.x + 5),
                    destY = Random.nextInt(first.location.z - 5, first.location.z + 5),
                    z = it.location.level
                )
                it.chat(it.rights, 0) { "Hello Xlite." }
                it.spotAnimate { 574 }
                it.hit(HitBar.DEFAULT, null, HitType.values().random(), 0) { Random.nextInt(1, 127) }
                it.route {
                    val list = IntArrayList(path.coords.size)
                    path.coords.forEach { c -> list.add(Location(c.x, c.y, it.location.level).packedLocation) }
                    list
                }
            }
        }

        val playerSyncFirstBlock = measureTime {
            players.forEach {
                it.invokeAndClearReadPool()
                it.syncMovement(syncPlayers)
                it.syncRenderingBlocks()
            }
        }

        val npcs = world.npcs()

        val npcFindersTime = measureTime {
            npcs.forEach {
                val path = dumbPathFinder.findPath(
                    srcX = it.location.x,
                    srcY = it.location.z,
                    destX = Random.nextInt(it.location.x - 5, it.location.x + 5),
                    destY = Random.nextInt(it.location.z - 5, it.location.z + 5),
                    z = it.location.level
                )
                it.route {
                    val list = IntArrayList(path.coords.size)
                    path.coords.forEach { c -> list.add(Location(c.x, c.y, it.location.level).packedLocation) }
                    list
                }
            }
        }

        val npcSyncFirstBlock = measureTime {
            npcs.forEach {
                it.syncMovement(syncPlayers)
                it.syncRenderingBlocks()
            }
        }

        val clientSyncTime = measureTime {
            players.forEach {
                it.syncZones()
                it.syncClient(syncPlayers)
            }
            ZoneUpdates.forEach(Zone::finalizeUpdateRequests)
        }

        logger.debug { "Players Pathfinders Took $playerFindersTime for ${players.size} players. [TICK=$tick]" }
        logger.debug { "Players Sync First Block Took $playerSyncFirstBlock for ${players.size} players. [TICK=$tick]" }
        logger.debug { "Npcs Pathfinders Took $npcFindersTime for ${npcs.size} npcs.  [TICK=$tick]" }
        logger.debug { "Npcs Sync First Block Took $npcSyncFirstBlock for ${npcs.size} npcs. [TICK=$tick]" }
        logger.debug { "Client Sync Took $clientSyncTime for ${players.size} players. [TICK=$tick]" }
    }
}
