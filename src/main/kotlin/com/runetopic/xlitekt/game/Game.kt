package com.runetopic.xlitekt.game

import com.runetopic.xlitekt.game.event.EventBus
import com.runetopic.xlitekt.game.world.World
import com.runetopic.xlitekt.game.world.engine.LoopTask
import com.runetopic.xlitekt.shared.resource.Resource.interfaceInfoResource
import com.runetopic.xlitekt.shared.resource.Resource.mapSquaresResource
import com.runetopic.xlitekt.shared.resource.Resource.sequencesResource
import com.runetopic.xlitekt.shared.resource.Resource.spotAnimationsResource
import com.runetopic.xlitekt.shared.resource.Resource.varBitsResource
import com.runetopic.xlitekt.shared.resource.Resource.varpsResource
import org.koin.dsl.module

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
    private val loop = LoopTask()

    fun start() {
        loop.start()
    }

    fun shutdown() {
        loop.shutdown()
    }
}
