package xlitekt.game

import org.koin.dsl.module
import org.rsmod.pathfinder.ZoneFlags
import xlitekt.game.actor.render.block.NPCRenderingBlockListener
import xlitekt.game.actor.render.block.PlayerRenderingBlockListener
import xlitekt.game.actor.render.block.body.BodyPartBlockListener
import xlitekt.game.content.command.CommandListener
import xlitekt.game.content.ui.InterfaceListener
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
    single { PlayerRenderingBlockListener() }
    single { NPCRenderingBlockListener() }
    single { BodyPartBlockListener() }
    single { InterfaceListener() }
    single { CommandListener() }
    single { EventBus() }
    single { World() }
    single { ZoneFlags() }
    single { Game() }
    single { PlayerJsonEncoderService(Executors.newSingleThreadScheduledExecutor()) }
    single { Zones() }
}
