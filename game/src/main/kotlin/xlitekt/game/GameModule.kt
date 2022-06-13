package xlitekt.game

import org.koin.dsl.module
import org.rsmod.pathfinder.ZoneFlags
import xlitekt.game.event.EventBus
import xlitekt.game.fs.PlayerJsonEncoderService
import xlitekt.game.world.World
import xlitekt.game.world.map.zone.Zones
import java.util.concurrent.Executors

/**
 * @author Jordan Abraham
 */
val gameModule = module(createdAtStart = true) {
    single { EventBus() }
    single { World() }
    single { ZoneFlags() }
    single { Game() }
    single { PlayerJsonEncoderService(Executors.newSingleThreadScheduledExecutor()) }
    single { Zones() }
}
