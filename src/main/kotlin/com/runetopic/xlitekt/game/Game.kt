package com.runetopic.xlitekt.game

import com.github.michaelbull.logging.InlineLogger
import com.runetopic.xlitekt.game.event.EventBus
import com.runetopic.xlitekt.game.world.World
import com.runetopic.xlitekt.plugin.koin.inject
import com.runetopic.xlitekt.util.resource.loadAllMapSquares
import com.runetopic.xlitekt.util.resource.loadAllSequences
import com.runetopic.xlitekt.util.resource.loadAllSpotAnimations
import com.runetopic.xlitekt.util.resource.loadAllVarBits
import com.runetopic.xlitekt.util.resource.loadAllVarps
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.time.measureTime

val gameModule = module(createdAtStart = true) {
    single(named("mapsquares")) { loadAllMapSquares() }
    single(named("sequences")) { loadAllSequences() }
    single(named("spotanimations")) { loadAllSpotAnimations() }
    single(named("varps")) { loadAllVarps() }
    single(named("varbits")) { loadAllVarBits() }
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
            val time = measureTime { world.process() }
            logger.debug { "Main game loop took $time to finish." }
        }, 0, 600, TimeUnit.MILLISECONDS)
    }

    fun shutdownGracefully() {
        logger.debug { "Shutting down game loop..." }
        service.shutdown()
    }
}
