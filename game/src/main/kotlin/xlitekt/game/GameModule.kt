package xlitekt.game

import org.koin.dsl.module
import xlitekt.game.event.EventBus
import xlitekt.game.world.World

/**
 * @author Jordan Abraham
 */
val gameModule = module(createdAtStart = true) {
    single { World() }
    single { Game() }
    single { EventBus() }
}
