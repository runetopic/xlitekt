package xlitekt.game

import io.ktor.server.application.ApplicationEnvironment
import org.koin.dsl.module
import org.rsmod.pathfinder.ZoneFlags
import xlitekt.game.event.EventBus
import xlitekt.game.fs.PlayerJsonEncoderService
import xlitekt.game.tick.ParallelClientSynchronizer
import xlitekt.game.tick.SequentialClientSynchronizer
import xlitekt.game.tick.WorldLoginSynchronizer
import xlitekt.game.tick.WorldLogoutSynchronizer
import xlitekt.game.tick.benchmark.BenchmarkParallelClientSynchronizer
import xlitekt.game.world.World
import xlitekt.game.world.map.zone.Zones
import xlitekt.shared.lazy
import java.util.concurrent.Executors

/**
 * @author Jordan Abraham
 */
val gameModule = module(createdAtStart = true) {
    single { EventBus() }
    single { World() }
    single { ZoneFlags() }
    single { Game() }
    single {
        val benchmarking = lazy<ApplicationEnvironment>().config.property("game.benchmarking").getString().toBoolean()
        val threads = Runtime.getRuntime().availableProcessors()

        val mainSynchronizer = when {
            benchmarking -> if (threads >= 4) BenchmarkParallelClientSynchronizer() else throw IllegalStateException("Must have 4 or more threads to use BenchmarkSynchronizationTask. Please disable in application.conf.")
            threads >= 4 -> ParallelClientSynchronizer()
            else -> SequentialClientSynchronizer()
        }
        GameLoop(
            Executors.newSingleThreadScheduledExecutor(),
            listOf(
                WorldLogoutSynchronizer(),
                WorldLoginSynchronizer(),
                mainSynchronizer
            )
        )
    }
    single { PlayerJsonEncoderService(Executors.newSingleThreadScheduledExecutor()) }
    single { Zones() }
}
