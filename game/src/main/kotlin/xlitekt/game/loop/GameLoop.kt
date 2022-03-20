package xlitekt.game.loop

import io.ktor.application.ApplicationEnvironment
import xlitekt.game.loop.task.BenchmarkSynchronizationTask
import xlitekt.game.loop.task.ParallelSynchronizationTask
import xlitekt.game.loop.task.SequentialSynchronizationTask
import xlitekt.game.loop.task.SynchronizationTask
import xlitekt.shared.inject
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * @author Jordan Abraham
 */
class GameLoop {

    private val benchmarking = inject<ApplicationEnvironment>().value.config.property("game.benchmarking").getString().toBoolean()
    private val threads = Runtime.getRuntime().availableProcessors()
    private val executor = Executors.newSingleThreadScheduledExecutor()

    fun start() {
        val task = when {
            benchmarking -> if (threads >= 4) BenchmarkSynchronizationTask() else throw IllegalStateException("Must have 4 or more threads to use BenchmarkSynchronizationTask. Please disable in application.conf.")
            threads >= 4 -> ParallelSynchronizationTask()
            else -> SequentialSynchronizationTask()
        }
        executor.scheduleAtFixedRate(SynchronizationTask(task), 600, 600, TimeUnit.MILLISECONDS)
    }

    fun shutdown() {
        executor.shutdown()
    }
}
