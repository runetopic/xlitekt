package script.command

import xlitekt.game.content.command.CommandListener
import xlitekt.game.world.World
import xlitekt.shared.inject
import xlitekt.shared.insert

/**
 * @author Jordan Abraham
 */
private val world by inject<World>()

insert<CommandListener>().command("kickall").use {
    world.players().filter { it != this }.onEach(world::requestLogout)
}
