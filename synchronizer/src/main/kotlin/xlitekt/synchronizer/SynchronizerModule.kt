package xlitekt.synchronizer

import io.ktor.server.application.ApplicationEnvironment
import org.koin.dsl.module
import xlitekt.shared.insert
import java.util.concurrent.Executors
import java.util.concurrent.ForkJoinPool
import java.util.concurrent.TimeUnit

/**
 * @author Jordan Abraham
 */
val synchronizerModule = module(createdAtStart = true) {
    single {
        val configuration = insert<ApplicationEnvironment>().config
        val forkJoinPool = ForkJoinPool(configuration.property("game.cores").getString().toInt())
        val benchmarking = configuration.property("game.benchmarking").getString().toBoolean()
        val synchronizer = if (benchmarking) SynchronizerBenchmark(forkJoinPool) else Synchronizer(forkJoinPool)
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(synchronizer, 600, 600, TimeUnit.MILLISECONDS)
    }
}
