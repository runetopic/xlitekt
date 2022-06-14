package xlitekt.synchronizer

import com.github.michaelbull.logging.InlineLogger
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import xlitekt.game.Game
import xlitekt.game.actor.npc.NPC
import xlitekt.game.actor.player.Player
import xlitekt.game.world.World
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
import kotlin.time.measureTime

/**
 * @author Jordan Abraham
 */
class Synchronizer(
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

    override fun run() {
        try {
            if (!game.online) return

            if (game.shuttingdown) {
                if (!forkJoinPool.isShutdown) forkJoinPool.shutdown()
                return
            }

            val players = world.players()
            val npcs = world.npcs()

            val time = measureTime {
                val syncPlayers = world.playersMapped()

                runBlocking(dispatcher) {
                    awaitAll(
                        async { logoutsSynchronizerTask.execute(syncPlayers, players) },
                        async { loginsSynchronizerTask.execute(syncPlayers, players) }
                    )

                    playersSynchronizerTask.execute(syncPlayers, players)
                    npcsSynchronizerTask.execute(syncPlayers, npcs)
                    zonesSynchronizerTask.execute(syncPlayers, players)

                    awaitAll(
                        async { playersBuildersTask.execute(syncPlayers, players) },
                        async { npcsBuildersTask.execute(syncPlayers, npcs) },
                    )

                    clientsSynchronizerTask.execute(syncPlayers, players)

                    awaitAll(
                        async { zonesSynchronizerTask.finish() },
                        async { clientsSynchronizerTask.finish() }
                    )
                }
            }
            logger.debug { "[Synchronizer] [$time] [Threads=${forkJoinPool.parallelism}] [Players=${players.size}] [Npcs=${npcs.size}]" }
        } catch (exception: Exception) {
            logger.error(exception) { "Exception caught in synchronizer." }
        }
    }

    private companion object {
        val logger = InlineLogger()
        val game by inject<Game>()
        val world by inject<World>()
    }
}
