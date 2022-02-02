package com.runetopic.xlitekt.game

import com.github.michaelbull.logging.InlineLogger
import com.runetopic.xlitekt.game.event.EventBus
import com.runetopic.xlitekt.game.world.World
import com.runetopic.xlitekt.plugin.ktor.inject
import com.runetopic.xlitekt.util.resource.loadAllMapSquares
import org.koin.dsl.module
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.time.measureTime

val gameModule = module {
    single { loadAllMapSquares() }
    single { World() }
    single { Game() }
    single { EventBus() }
}

class Game {
    private val world by inject<World>()
    private val service = Executors.newScheduledThreadPool(1)
    private val logger = InlineLogger()

    fun start() {
        service.scheduleAtFixedRate({
            val time = measureTime { world.process() }
            logger.info { "Loop took $time to complete." }
        }, 0, 600, TimeUnit.MILLISECONDS)
    }

    fun shutdownGracefully() {
        logger.debug { "Shutting down game loop..." }
        service.shutdown()
    }
}
