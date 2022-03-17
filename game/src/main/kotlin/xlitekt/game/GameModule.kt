package xlitekt.game

import org.koin.dsl.module
import org.koin.dsl.single
import org.rsmod.pathfinder.ZoneFlags
import xlitekt.game.event.EventBus
import xlitekt.game.world.World

/**
 * @author Jordan Abraham
 */
val gameModule = module(createdAtStart = true) {
    single { World() }
    single { ZoneFlags() }
    single { Game() }
    single { EventBus() }
}
