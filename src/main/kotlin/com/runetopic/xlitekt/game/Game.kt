package com.runetopic.xlitekt.game

import com.github.michaelbull.logging.InlineLogger
import com.runetopic.xlitekt.game.event.EventBus
import com.runetopic.xlitekt.game.world.World
import com.runetopic.xlitekt.game.world.engine.LoopTask
import com.runetopic.xlitekt.network.client.Client.Companion.world
import com.runetopic.xlitekt.plugin.koin.inject
import com.runetopic.xlitekt.shared.resource.Resource.interfaceInfoResource
import com.runetopic.xlitekt.shared.resource.Resource.mapSquaresResource
import com.runetopic.xlitekt.shared.resource.Resource.sequencesResource
import com.runetopic.xlitekt.shared.resource.Resource.spotAnimationsResource
import com.runetopic.xlitekt.shared.resource.Resource.varBitsResource
import com.runetopic.xlitekt.shared.resource.Resource.varpsResource
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
    single { interfaceInfoResource() }
    single { World() }
    single { Game() }
    single { EventBus() }
}

class Game {
    private val logger = InlineLogger()
    private val loop = LoopTask()

//    private val service = Executors.newSingleThreadScheduledExecutor()
//    private val world by inject<World>()

    fun start() {
        loop.start()
//        service.scheduleAtFixedRate({
//            val time = measureTime(world::process)
//            logger.debug { "Main game loop took $time to finish." }
//        }, 0, 600, TimeUnit.MILLISECONDS)
    }

    fun shutdownGracefully() {
        loop.shutdown()
//        logger.debug { "Shutting down game loop..." }
//        service.shutdown()
    }
}
