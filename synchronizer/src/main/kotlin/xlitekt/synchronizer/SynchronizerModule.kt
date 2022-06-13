package xlitekt.synchronizer

import io.ktor.server.application.ApplicationEnvironment
import org.koin.dsl.module
import xlitekt.shared.lazy
import java.util.concurrent.Executors
import java.util.concurrent.ForkJoinPool
import java.util.concurrent.TimeUnit

/**
 * @author Jordan Abraham
 */
val synchronizerModule = module(createdAtStart = true) {
    val executor = Executors.newSingleThreadScheduledExecutor()
    val forkJoinPool = ForkJoinPool(Runtime.getRuntime().availableProcessors())
    single {
        val benchmarking = lazy<ApplicationEnvironment>().config.property("game.benchmarking").getString().toBoolean()
        val synchronizer = if (benchmarking) SynchronizerBenchmark(forkJoinPool) else Synchronizer(forkJoinPool)
        executor.scheduleAtFixedRate(synchronizer, 600, 600, TimeUnit.MILLISECONDS)
    }
}
