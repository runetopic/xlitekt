package xlitekt.synchronizer

import com.github.michaelbull.logging.InlineLogger
import org.jctools.maps.NonBlockingHashSet
import xlitekt.game.Game
import xlitekt.game.actor.chat
import xlitekt.game.actor.hit
import xlitekt.game.actor.npc.NPC
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.render.HitBar
import xlitekt.game.actor.render.HitType
import xlitekt.game.actor.routeTo
import xlitekt.game.actor.spotAnimate
import xlitekt.game.world.World
import xlitekt.game.world.map.Location
import xlitekt.game.world.map.zone.Zone
import xlitekt.shared.inject
import xlitekt.synchronizer.builder.MovementUpdatesBuilder
import xlitekt.synchronizer.builder.NpcHighDefinitionUpdatesBuilder
import xlitekt.synchronizer.builder.PlayerAlternativeHighDefinitionUpdatesBuilder
import xlitekt.synchronizer.builder.PlayerAlternativeLowDefinitionUpdatesBuilder
import xlitekt.synchronizer.builder.PlayerHighDefinitionUpdatesBuilder
import xlitekt.synchronizer.builder.PlayerLowDefinitionUpdatesBuilder
import xlitekt.synchronizer.task.ClientsSynchronizerTask
import xlitekt.synchronizer.task.LoginsSynchronizerTask
import xlitekt.synchronizer.task.LogoutsSynchronizerTask
import xlitekt.synchronizer.task.NpcsSynchronizerTask
import xlitekt.synchronizer.task.PlayersSynchronizerTask
import xlitekt.synchronizer.task.ZonesSynchronizerTask
import java.util.concurrent.ForkJoinPool
import kotlin.random.Random
import kotlin.time.measureTime

/**
 * @author Jordan Abraham
 */
class SynchronizerBenchmark(
    private val forkJoinPool: ForkJoinPool
) : Runnable {
    private val logger = InlineLogger()

    private val game by inject<Game>()
    private val world by inject<World>()

    private val loginsSynchronizerTask = LoginsSynchronizerTask()
    private val logoutsSynchronizerTask = LogoutsSynchronizerTask()

    private val playerMovementUpdatesBuilder = MovementUpdatesBuilder<Player>()
    private val playerHighDefinitionUpdatesBuilder = PlayerHighDefinitionUpdatesBuilder()
    private val playerLowDefinitionUpdatesBuilder = PlayerLowDefinitionUpdatesBuilder()
    private val playerAlternativeHighDefinitionUpdatesBuilder = PlayerAlternativeHighDefinitionUpdatesBuilder()
    private val playerAlternativeLowDefinitionUpdatesBuilder = PlayerAlternativeLowDefinitionUpdatesBuilder()

    private val npcMovementUpdatesBuilder = MovementUpdatesBuilder<NPC>()
    private val npcHighDefinitionUpdatesBuilder = NpcHighDefinitionUpdatesBuilder()

    private val zoneUpdates = NonBlockingHashSet<Zone>()

    private val playersSynchronizerTask = PlayersSynchronizerTask(
        listOf(
            playerMovementUpdatesBuilder,
            playerHighDefinitionUpdatesBuilder,
            playerLowDefinitionUpdatesBuilder,
            playerAlternativeHighDefinitionUpdatesBuilder,
            playerAlternativeLowDefinitionUpdatesBuilder
        )
    )

    private val npcsSynchronizerTask = NpcsSynchronizerTask(
        listOf(
            npcMovementUpdatesBuilder,
            npcHighDefinitionUpdatesBuilder
        )
    )

    private val zonesSynchronizerTask = ZonesSynchronizerTask(
        zoneUpdates
    )

    private val clientsSynchronizerTask = ClientsSynchronizerTask(
        playerMovementUpdatesBuilder,
        playerHighDefinitionUpdatesBuilder,
        playerLowDefinitionUpdatesBuilder,
        playerAlternativeHighDefinitionUpdatesBuilder,
        playerAlternativeLowDefinitionUpdatesBuilder,
        npcHighDefinitionUpdatesBuilder,
        npcMovementUpdatesBuilder
    )

    private var tick = 0

    override fun run() {
        try {
            if (!game.online) return

            if (game.shuttingdown) {
                if (!forkJoinPool.isShutdown) forkJoinPool.shutdown()
                return
            }

            tick++
            val time = measureTime {
                val players = world.players()
                val npcs = world.npcs()
                val syncPlayers = world.playersMapped()

                val job = forkJoinPool.submit {
                    logoutsSynchronizerTask.execute(syncPlayers, players)
                    loginsSynchronizerTask.execute(syncPlayers, players)

                    val playerFindersTime = measureTime {
                        val first = players.firstOrNull()
                        players.filter { it != first }.parallelStream().forEach {
                            it.chat(it.rights, 0) { "Hello Xlite." }
                            it.spotAnimate { 574 }
                            it.hit(HitBar.DEFAULT, null, HitType.values().random(), 0) { Random.nextInt(1, 127) }
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
                        playersSynchronizerTask.execute(syncPlayers, players)
                    }

                    val npcFindersTime = measureTime {
                        npcs.parallelStream().forEach {
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
                        npcsSynchronizerTask.execute(syncPlayers, npcs)
                    }

                    val zonesTime = measureTime {
                        zonesSynchronizerTask.execute(syncPlayers, players)
                    }

                    val clientSyncTime = measureTime {
                        clientsSynchronizerTask.execute(syncPlayers, players)
                    }
                    zonesSynchronizerTask.finish()
                    clientsSynchronizerTask.finish()

                    logger.debug { "Players Pathfinders Took $playerFindersTime for ${players.size} players. [TICK=$tick]" }
                    logger.debug { "Players Sync First Block Took $playerSyncFirstBlock for ${players.size} players. [TICK=$tick]" }
                    logger.debug { "Npcs Pathfinders Took $npcFindersTime for ${npcs.size} npcs.  [TICK=$tick]" }
                    logger.debug { "Npcs Sync First Block Took $npcSyncFirstBlock for ${npcs.size} npcs. [TICK=$tick]" }
                    logger.debug { "Zones Sync Took $zonesTime. [TICK=$tick]" }
                    logger.debug { "Client Sync Took $clientSyncTime for ${players.size} players. [TICK=$tick]" }
                }

                job.get()
            }
            logger.debug { "Synchronizer completed in $time targeting ${forkJoinPool.parallelism} threads." }
        } catch (exception: Exception) {
            logger.error(exception) { "Exception caught in synchronizer." }
        }
    }
}
