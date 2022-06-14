package xlitekt.synchronizer

import com.github.michaelbull.logging.InlineLogger
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
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
import xlitekt.synchronizer.task.NpcsBuildersTask
import xlitekt.synchronizer.task.NpcsSynchronizerTask
import xlitekt.synchronizer.task.PlayersBuildersTask
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
    private val dispatcher = forkJoinPool.asCoroutineDispatcher()

    private val loginsSynchronizerTask = LoginsSynchronizerTask()
    private val logoutsSynchronizerTask = LogoutsSynchronizerTask()
    private val playersSynchronizerTask = PlayersSynchronizerTask()
    private val npcsSynchronizerTask = NpcsSynchronizerTask()

    private val playerMovementUpdatesBuilder = MovementUpdatesBuilder<Player>()
    private val playerHighDefinitionUpdatesBuilder = PlayerHighDefinitionUpdatesBuilder()
    private val playerLowDefinitionUpdatesBuilder = PlayerLowDefinitionUpdatesBuilder()
    private val playerAlternativeHighDefinitionUpdatesBuilder = PlayerAlternativeHighDefinitionUpdatesBuilder()
    private val playerAlternativeLowDefinitionUpdatesBuilder = PlayerAlternativeLowDefinitionUpdatesBuilder()

    private val npcMovementUpdatesBuilder = MovementUpdatesBuilder<NPC>()
    private val npcHighDefinitionUpdatesBuilder = NpcHighDefinitionUpdatesBuilder()

    private val playersBuildersTask = PlayersBuildersTask(
        listOf(
            playerMovementUpdatesBuilder,
            playerHighDefinitionUpdatesBuilder,
            playerLowDefinitionUpdatesBuilder,
            playerAlternativeHighDefinitionUpdatesBuilder,
            playerAlternativeLowDefinitionUpdatesBuilder
        )
    )

    private val npcsBuildersTask = NpcsBuildersTask(
        listOf(
            npcMovementUpdatesBuilder,
            npcHighDefinitionUpdatesBuilder
        )
    )

    private val zonesSynchronizerTask = ZonesSynchronizerTask()

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

            val players = world.players()
            val npcs = world.npcs()

            val time = measureTime {
                val syncPlayers = world.playersMapped()

                runBlocking(dispatcher) {
                    val loginsLogoutsTime = measureTime {
                        awaitAll(
                            async { logoutsSynchronizerTask.execute(syncPlayers, players) },
                            async { loginsSynchronizerTask.execute(syncPlayers, players) }
                        )
                    }
                    logger.debug { "[$tick] [Logins and Logouts] [$loginsLogoutsTime]" }

                    val forceUpdatesTime = measureTime {
                        awaitAll(
                            async { forcePlayerUpdates(tick, players) },
                            async { forceNpcUpdates(tick, npcs) }
                        )
                    }
                    logger.debug { "[$tick] [Player & Npc Updates] [$forceUpdatesTime]" }

                    val logicBlock = measureTime {
                        playersSynchronizerTask.execute(syncPlayers, players)
                        npcsSynchronizerTask.execute(syncPlayers, npcs)
                        zonesSynchronizerTask.execute(syncPlayers, players)
                    }
                    logger.debug { "[$tick] [Logic] [$logicBlock]" }

                    val buildersTime = measureTime {
                        awaitAll(
                            async { playersBuildersTask.execute(syncPlayers, players) },
                            async { npcsBuildersTask.execute(syncPlayers, npcs) },
                        )
                    }
                    logger.debug { "[$tick] [Player & Npc Builders] [$buildersTime]" }

                    val clientsTime = measureTime {
                        clientsSynchronizerTask.execute(syncPlayers, players)
                    }
                    logger.debug { "[$tick] [Clients] [$clientsTime]" }

                    awaitAll(
                        async { zonesSynchronizerTask.finish() },
                        async { clientsSynchronizerTask.finish() }
                    )
                }
            }
            logger.debug { "[$tick] [Synchronizer] [$time] [Threads=${forkJoinPool.parallelism}] [Players=${players.size}] [Npcs=${npcs.size}]" }
        } catch (exception: Exception) {
            logger.error(exception) { "Exception caught in synchronizer." }
        }
    }

    private companion object {
        val logger = InlineLogger()
        val game by inject<Game>()
        val world by inject<World>()

        fun forcePlayerUpdates(tick: Int, players: List<Player>) {
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
            logger.debug { "[$tick] [Players Pathfinders] [$playerFindersTime]" }
        }

        fun forceNpcUpdates(tick: Int, npcs: List<NPC>) {
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
            logger.debug { "[$tick] [Npcs Pathfinders] [$npcFindersTime]" }
        }
    }
}
