package xlitekt.game.world.engine

import xlitekt.game.world.engine.sync.PlayerSynchronizer
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * @author Jordan Abraham
 */
class LoopTask : Task {

    private val executor = Executors.newSingleThreadScheduledExecutor()
    private val playerSynchronizer = PlayerSynchronizer()

    override fun start() {
        executor.scheduleAtFixedRate(playerSynchronizer, 600, 600, TimeUnit.MILLISECONDS)
    }

    override fun shutdown() {
        executor.shutdown()
    }
}
