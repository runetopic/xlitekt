package xlitekt.game.loop.sync

import com.github.michaelbull.logging.InlineLogger
import kotlin.time.measureTime

/**
 * @author Jordan Abraham
 */
class PhasedSynchronizer(
    private val synchronizers: List<Runnable>
) : Synchronizer() {
    private val logger = InlineLogger()

    override fun run() {
        try {
            val time = measureTime {
                synchronizers.forEach(Runnable::run)
            }
            logger.debug { "Synchronization completed in $time." }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }
}
