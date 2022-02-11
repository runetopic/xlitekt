package com.runetopic.xlitekt.game

import com.github.michaelbull.logging.InlineLogger
import com.runetopic.xlitekt.game.event.EventBus
import com.runetopic.xlitekt.game.world.World
import com.runetopic.xlitekt.plugin.koin.inject
import com.runetopic.xlitekt.util.resource.mapSquaresResource
import com.runetopic.xlitekt.util.resource.sequencesResource
import com.runetopic.xlitekt.util.resource.spotAnimationsResource
import com.runetopic.xlitekt.util.resource.varBitsResource
import com.runetopic.xlitekt.util.resource.varpsResource
import org.koin.dsl.module
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.time.measureTime

val gameModule = module(createdAtStart = true) {
    single { mapSquaresResource() }
    single { sequencesResource() }
    single { spotAnimationsResource() }
    single { varpsResource() }
    single { varBitsResource() }
    single { World() }
    single { Game() }
    single { EventBus() }
}

class Game {
    private val logger = InlineLogger()
    private val service = Executors.newSingleThreadScheduledExecutor()
    private val world by inject<World>()

    fun start() {
        service.scheduleAtFixedRate({
            val time = measureTime(world::process)
            logger.debug { "Main game loop took $time to finish." }
        }, 0, 600, TimeUnit.MILLISECONDS)
    }

    fun shutdownGracefully() {
        logger.debug { "Shutting down game loop..." }
        service.shutdown()
    }
}
