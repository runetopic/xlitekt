package xlitekt.game.loop.task

import com.github.michaelbull.logging.InlineLogger
import kotlin.time.measureTime

/**
 * @author Jordan Abraham
 */
class SynchronizationTask(
    val task: Runnable
) : Task() {
    private val logger = InlineLogger()

    override fun run() {
        try {
            val time = measureTime {
                world.processLoginRequests()
                world.processLogoutRequests()
                task.run()
            }
            logger.debug { "Took $time to complete $task" }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }
}
