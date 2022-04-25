package xlitekt.game

import xlitekt.game.tick.PhasedSynchronizer
import xlitekt.game.tick.Synchronizer
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

/**
 * @author Jordan Abraham
 */
class GameLoop(
    private val executor: ScheduledExecutorService,
    private val synchronizers: List<Synchronizer>
) {
    fun start(): ScheduledFuture<*> = executor.scheduleAtFixedRate(PhasedSynchronizer(synchronizers), 600, 600, TimeUnit.MILLISECONDS)
    fun shutdown() = executor.shutdown()
}
