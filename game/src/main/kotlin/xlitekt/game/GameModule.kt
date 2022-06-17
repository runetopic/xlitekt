package xlitekt.game

import org.koin.dsl.module
import org.rsmod.pathfinder.ZoneFlags
import xlitekt.game.event.EventBus
import xlitekt.game.fs.PlayerJsonEncoderService
import xlitekt.game.packet.assembler.PacketAssemblerListener
import xlitekt.game.packet.disassembler.PacketDisassemblerListener
import xlitekt.game.packet.disassembler.handler.PacketHandlerListener
import xlitekt.game.world.World
import xlitekt.game.world.map.zone.Zones
import java.util.concurrent.Executors

/**
 * @author Jordan Abraham
 */
val gameModule = module(createdAtStart = true) {
    single { PacketAssemblerListener() }
    single { PacketDisassemblerListener() }
    single { PacketHandlerListener() }
    single { EventBus() }
    single { World() }
    single { ZoneFlags() }
    single { Game() }
    single { PlayerJsonEncoderService(Executors.newSingleThreadScheduledExecutor()) }
    single { Zones() }
}
