package com.runetopic.xlitekt.game

import com.github.michaelbull.logging.InlineLogger
import com.runetopic.xlitekt.game.display.listener.impl.optionsListener
import com.runetopic.xlitekt.game.event.EventBus
import com.runetopic.xlitekt.game.world.World
import com.runetopic.xlitekt.plugin.inject
import com.runetopic.xlitekt.util.resource.loadAllMapSquares
import org.koin.dsl.module
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

val gameModule = module {
    single { loadAllMapSquares() }
    single { World() }
    single { Game() }
    single { EventBus() }
}

class Game {
    private val logger = InlineLogger()
    private val service = Executors.newScheduledThreadPool(1)
    private val world by inject<World>()

    fun start() {
        optionsListener
        service.scheduleAtFixedRate({
            world.process()
        }, 0, 600, TimeUnit.MILLISECONDS)
    }

    fun shutdownGracefully() {
        logger.debug { "Shutting down game loop..." }
        service.shutdown()
    }
}
