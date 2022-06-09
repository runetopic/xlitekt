package xlitekt.game.tick.benchmark

import com.github.michaelbull.logging.InlineLogger
import xlitekt.game.actor.chat
import xlitekt.game.actor.hit
import xlitekt.game.actor.render.HitBar
import xlitekt.game.actor.render.HitType
import xlitekt.game.actor.routeTo
import xlitekt.game.actor.spotAnimate
import xlitekt.game.tick.Synchronizer
import xlitekt.game.tick.ZoneUpdates
import xlitekt.game.world.map.Location
import xlitekt.game.world.map.zone.Zone
import kotlin.random.Random
import kotlin.time.measureTime
import xlitekt.game.actor.resetMovement

/**
 * @author Jordan Abraham
 */
class BenchmarkParallelClientSynchronizer : Synchronizer() {

    private val logger = InlineLogger()
    private var tick = 0

    override fun run() {
        tick++
        val players = world.players()
        val syncPlayers = world.playersMapped()

        val playerFindersTime = measureTime {
            val first = players.firstOrNull()
            players.filter { it != first }.parallelStream().forEach {
                it.chat(it.rights, 0) { "Hello Xlite." }
                it.spotAnimate { 574 }
                it.hit(HitBar.DEFAULT, null, HitType.values().random(), 0) { Random.nextInt(1, 127) }
                it.resetMovement()
                it.routeTo(
                    Location(
                        Random.nextInt(first!!.location.x - 5, first.location.x + 5),
                        Random.nextInt(first.location.z - 5, first.location.z + 5),
                        0
                    )
                )
            }
        }

        val playerSyncFirstBlock = measureTime {
            players.parallelStream().forEach {
                it.invokeAndClearReadPool()
                it.syncMovement(syncPlayers)
                it.syncRenderingBlocks()
            }
        }

        val npcs = world.npcs()

        val npcFindersTime = measureTime {
            npcs.parallelStream().forEach {
                it.resetMovement()
                it.routeTo(
                    Location(
                        Random.nextInt(it.location.x - 5, it.location.x + 5),
                        Random.nextInt(it.location.z - 5, it.location.z + 5),
                        it.location.level
                    )
                )
            }
        }

        val npcSyncFirstBlock = measureTime {
            npcs.parallelStream().forEach {
                it.syncMovement(syncPlayers)
                it.syncRenderingBlocks()
            }
        }

        val clientSyncTime = measureTime {
            players.parallelStream().forEach {
                it.syncZones()
                it.syncClient(syncPlayers)
            }
            ZoneUpdates.parallelStream().forEach(Zone::finalizeUpdateRequests)
        }

        logger.debug { "Players Pathfinders Took $playerFindersTime for ${players.size} players. [TICK=$tick]" }
        logger.debug { "Players Sync First Block Took $playerSyncFirstBlock for ${players.size} players. [TICK=$tick]" }
        logger.debug { "Npcs Pathfinders Took $npcFindersTime for ${npcs.size} npcs.  [TICK=$tick]" }
        logger.debug { "Npcs Sync First Block Took $npcSyncFirstBlock for ${npcs.size} npcs. [TICK=$tick]" }
        logger.debug { "Client Sync Took $clientSyncTime for ${players.size} players. [TICK=$tick]" }
    }
}
