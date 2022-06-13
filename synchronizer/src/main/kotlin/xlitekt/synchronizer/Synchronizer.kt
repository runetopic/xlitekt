package xlitekt.synchronizer

import com.github.michaelbull.logging.InlineLogger
import xlitekt.game.Game
import xlitekt.game.actor.npc.NPC
import xlitekt.game.actor.player.Player
import xlitekt.game.world.World
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
import java.util.Collections
import java.util.concurrent.ForkJoinPool
import java.util.concurrent.TimeUnit
import kotlin.time.measureTime

/**
 * @author Jordan Abraham
 */
class Synchronizer(
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

    private val zoneUpdates = Collections.synchronizedSet(HashSet<Zone>())

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

    override fun run() {
        if (!game.online) return

        if (game.shuttingdown) {
            if (!forkJoinPool.isShutdown) forkJoinPool.shutdown()
            return
        }

        val time = measureTime {
            val players = world.players()
            val npcs = world.npcs()
            val syncPlayers = world.playersMapped()

            val job = forkJoinPool.submit {
                logoutsSynchronizerTask.execute(syncPlayers, players)
                loginsSynchronizerTask.execute(syncPlayers, players)
                playersSynchronizerTask.execute(syncPlayers, players)
                npcsSynchronizerTask.execute(syncPlayers, npcs)
                zonesSynchronizerTask.execute(syncPlayers, players)
                clientsSynchronizerTask.execute(syncPlayers, players)
                zonesSynchronizerTask.finish()
                clientsSynchronizerTask.finish()
            }

            job.get(600L, TimeUnit.MILLISECONDS)
        }
        logger.debug { "Synchronizer completed in $time targeting ${forkJoinPool.parallelism} threads." }
    }
}
