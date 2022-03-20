package xlitekt.game

import io.ktor.application.ApplicationEnvironment
import org.koin.dsl.module
import org.rsmod.pathfinder.ZoneFlags
import xlitekt.game.event.EventBus
import xlitekt.game.loop.Loop
import xlitekt.game.loop.sync.ParallelActorSynchronizer
import xlitekt.game.loop.sync.SequentialActorSynchronizer
import xlitekt.game.loop.sync.WorldLoginSynchronizer
import xlitekt.game.loop.sync.WorldLogoutSynchronizer
import xlitekt.game.loop.sync.benchmark.BenchmarkParallelActorSynchronizer
import xlitekt.game.world.World
import java.util.concurrent.Executors

/**
 * @author Jordan Abraham
 */
val gameModule = module(createdAtStart = true) {
    single { World() }
    single { ZoneFlags() }
    single { Game() }
    single { EventBus() }
    single {
        val benchmarking = inject<ApplicationEnvironment>().value.config.property("game.benchmarking").getString().toBoolean()
        val threads = Runtime.getRuntime().availableProcessors()

        val mainSynchronizer = when {
            benchmarking -> if (threads >= 4) BenchmarkParallelActorSynchronizer() else throw IllegalStateException("Must have 4 or more threads to use BenchmarkSynchronizationTask. Please disable in application.conf.")
            threads >= 4 -> ParallelActorSynchronizer()
            else -> SequentialActorSynchronizer()
        }
        Loop(
            Executors.newSingleThreadScheduledExecutor(),
            listOf(
                WorldLogoutSynchronizer(),
                WorldLoginSynchronizer(),
                mainSynchronizer
            )
        )
    }
}
