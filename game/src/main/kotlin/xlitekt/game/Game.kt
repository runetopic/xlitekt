package xlitekt.game

import org.koin.dsl.module
import xlitekt.game.event.EventBus
import xlitekt.game.world.World
import xlitekt.game.world.engine.LoopTask
import xlitekt.shared.resource.Resource.interfaceInfoResource
import xlitekt.shared.resource.Resource.mapSquaresResource
import xlitekt.shared.resource.Resource.sequencesResource
import xlitekt.shared.resource.Resource.spotAnimationsResource
import xlitekt.shared.resource.Resource.varBitsResource
import xlitekt.shared.resource.Resource.varpsResource

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
