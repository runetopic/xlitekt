package com.runetopic.xlitekt.game.world.engine

import com.runetopic.xlitekt.game.world.engine.sync.EntitySynchronizer
import com.runetopic.xlitekt.game.world.engine.sync.UpdateSynchronizer
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * @author Jordan Abraham
 */
class LoopTask : Task {

    private val executor = Executors.newSingleThreadScheduledExecutor()
    private val executor2 = Executors.newSingleThreadScheduledExecutor()
    private val entitySynchronizer = EntitySynchronizer()
    private val updateSynchronizer = UpdateSynchronizer()

    override fun start() {
        // executor2.scheduleAtFixedRate(updateSynchronizer, 600, 600, TimeUnit.MILLISECONDS)
        executor.scheduleAtFixedRate(entitySynchronizer, 600, 600, TimeUnit.MILLISECONDS)
    }

    override fun shutdown() {
        executor.shutdown()
    }
}
