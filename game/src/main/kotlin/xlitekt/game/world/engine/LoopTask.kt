package xlitekt.game.world.engine

import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * @author Jordan Abraham
 */
class LoopTask : Task {

    private val executor = Executors.newSingleThreadScheduledExecutor()
    private val gameLoop = GameLoop()

    override fun start() {
        executor.scheduleAtFixedRate(gameLoop, 600, 600, TimeUnit.MILLISECONDS)
    }

    override fun shutdown() {
        executor.shutdown()
    }
}
