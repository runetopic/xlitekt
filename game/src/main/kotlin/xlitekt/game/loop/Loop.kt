package xlitekt.game.loop

import xlitekt.game.loop.sync.PhasedSynchronizer
import xlitekt.game.loop.sync.Synchronizer
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

/**
 * @author Jordan Abraham
 */
class Loop(
    private val executor: ScheduledExecutorService,
    private val synchronizers: List<Synchronizer>
) {
    fun start(): ScheduledFuture<*> = executor.scheduleAtFixedRate(PhasedSynchronizer(synchronizers), 600, 600, TimeUnit.MILLISECONDS)
    fun shutdown() = executor.shutdown()
}
